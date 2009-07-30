<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
$toDelete=array();//是要删除操作的列表
$toExe=array();//是未经筛选的要执行的操作列表
$Exe=array();//是筛选之后的操作列表
//定义temp_operation,用来储存具体的操作！
class temp_operation
{
	public $lAddrID,$commandID; 
	public function temp_operation($_lAddrID,$_commandID)
	{
		$this->lAddrID=$_lAddrID;
		$this->commandID=$_commandID;
	}
}
/*
 * 这就是构建的图中的结点，每个结点有三个数据，即$todo,$list_accroding,$list_contrary.$todo是一个operation的实例，以后将入$toExe
 * 栈；$list_accroding是一个temp_operation实例数组，用来储存于当前结点直接正关联的操作；$list_contrary也是一个temp_operation的实例数组，用来
 * 储存与当前结点直接反关联的操作.
 */
class node
{
	public $todo,$list_accroding,$list_contrary;
	public function node($_lAddrID,$_commandID)
	{
		$this->todo=new temp_operation($_lAddrID,$_commandID);
		global $db;
		$selFormat="SELECT lAddrID_2,commandID_2 FROM association WHERE lAddrID_1='%s' AND commandID_1='%s' AND direction=1";
		$query=sprintf($selFormat,$_lAddrID,$_commandID);
		if($res=mysql_query($query,$db))
		{
			while($resObj=mysql_fetch_object($res))
				$this->list_accroding[]=$resObj;
		}
		else
			return ERR_SERVER_ERROR;
		$selFormat="SELECT lAddrID_2,commandID_2 FROM association WHERE lAddrID_1='%s' AND commandID_1='%s' AND direction=0";
		$query=sprintf($selFormat,$_lAddrID,$_commandID);
		if($res=mysql_query($query,$db))
		{
			while($resObj=mysql_fetch_object($res))
				$this->list_contrary[]=$resObj;
		}
		else
			return ERR_SERVER_ERROR;
	}
}
/*
 * 定义深度索搜的函数,用来对每个反关联进行深度搜索,索搜处与之正相关的操作,并将这些操作入$contrary栈.
 */
function DFS($_lAddrID,$_commandID)
{
	global $db;
	global $toDelete;
	global $toExe;
	$selFormat="SELECT lAddrID_2,commandID_2 FROM association WHERE lAddrID_1='%s' AND commandID_1='%s' AND direction=1";
	$query=sprintf($selFormat,$_lAddrID,$_commandID);
	if($res=mysql_query($query,$db))
	{
		$rs=array();
		while($resObj=mysql_fetch_object($res))
			$rs[]=$resObj;
	}
	else
		return ERR_SERVER_ERROR;
	foreach($rs as $temp)
	{
		if($temp)
		{
			$flag=1;
			/*
			 * 在将反关联操作的正关联操作入$toDelete栈之前,要先检查该正关联操作是否已经出现在了$toDelete栈中!
			 */
			foreach($toDelete as $check_delete)
			{
				if($temp->lAddrID_2==$check_delete->lAddrID_2 && $temp->commandID_2==$check_delete->commandID_2)
				{
					$flag=0;
				}
			}
			foreach($toExe as $check_exe)
			{
				if($temp->lAddrID_2==$check_exe->lAddrID && $temp->commandID_2==$check_exe->commandID)
				{
					$flag=0;
				}
			}
			if($flag)
			{
				$toDelete[]=$temp;
				DFS($temp->lAddrID_2,$temp->commandID_2);
			}
		}
	}
}
//定义getList函数,来得到初始栈，也是进行筛选的范围.要出栈的元素位于数组的尾部,栈的顶部.
function getList()
{
	global $db;
	$rs=Array();
	$selFormat='SELECT * FROM todo';
	$query=sprintf($selFormat);
	if($res=mysql_query($query,$db))
	{
		while($resObj=mysql_fetch_object($res))
			$rs[]=$resObj;
	}
	else
		return ERR_SERVER_ERROR;
	$selFormat='SELECT * FROM manual';
	$query=sprintf($selFormat);
	if($res=mysql_query($query,$db))
	{
		while($resObj=mysql_fetch_object($res))
			$rs[]=$resObj;
		return $rs;
	}
	else
		return ERR_SERVER_ERROR;
}
function finalExe()
{
	global $toDelete;
	global $Exe;
	global $toExe;
	$result=getList();//运用getList函数,得到初始栈.
	$i=count($result)-1;//定义$i,得到栈顶的指针!
	$top=$result[$i];//栈顶元素出栈！
	while($top)
	{
		/*
		 * 一定要判断$top中是lAddrID与commandID还是lAddrID_2与commandID_2,二者在生成结点时传递参数的方式不同!
		 */
		if(isset($top->lAddrID))
		{
			$root=new node($top->lAddrID,$top->commandID);
		}
		else
		{
			$root=new node($top->lAddrID_2,$top->commandID_2);
		}
		unset($result[$i]);
		$i=$i-1;//栈顶元素要出栈,就要将将原来的栈顶元素unset掉,并且将栈顶指针下退一格！这两个操作要在生成结点之后进行!
		if($root->list_contrary)
		{
			foreach($root->list_contrary as $temp_contrary)
			{
				$flag=1;
				foreach($toExe as $check)
				{
					if($check->lAddrID==$temp_contrary->lAddrID_2 && $check->commandID==$temp_contrary->commandID_2)
					$flag=0;
				}
				if($flag)
				{
					$toDelete[]=$temp_contrary;
					DFS($temp_contrary->lAddrID_2,$temp_contrary->commandID_2);
				}
			}
		}//这里实现的是将与当前结点相反关联的操作的正关联操作入$toDelete栈,即是要删除的操作列表！主意到$temp_contrary中的元素名字是$lAddrID_2与$commandID_2!!!千万不要弄错了,否则用不了!!!
		if($root->list_accroding)
		{
			foreach($root->list_accroding as $temp_according)
			{
				$flag=1;
				foreach($toDelete as $temp_delete)
				{
					if($temp_according->lAddrID_2==$temp_delete->lAddrID_2 && $temp_according->commandID_2==$temp_delete->commandID_2)
					$flag=0;
				}
				foreach($toExe as $check)
				{
					if($check->lAddrID==$temp_according->lAddrID_2 && $check->commandID==$temp_according->commandID_2)
					$flag=0;
				}
				if($flag)
				{
					//array_push($result,$temp_according);//这样使用的时候会出问题！！！数组会直接跳过刚才被unset掉的下标，直接给下一个数组元素赋值！！！
					$result[$i+1]=$temp_according;
					$i=$i+1;
				}
			}
		}//这里实现的是将栈顶元素的正关联操作入栈!在入栈之前还要先检查该操作是否在$toDelete栈中出现过！！！更重要的是检查该操作是否在执行列表$toExe中出现过，否则会出现死循环！
		/*
		 * 现在要将结点本身的操作入操作列表栈$toExe，入栈之前先检查它是否在要删除的操作列表$toDelete中出现过！
		 */
		if($toDelete)
		{
			foreach($toDelete as $temp_delete)
			{
				$flag=1;
				if($root->todo->lAddrID==$temp_delete->lAddrID_2 && $root->todo->commandID==$temp_delete->commandID_2)
				{
					$flag=0;
				}
				if($flag)
				{
					$toExe[]=$root->todo;
				}
			}
		}
		else
		{
			$toExe[]=$root->todo;
		}//这里实现的是将出栈的元素加入到将要执行的操作中!加入之前要先检查一下这个操作知否在$toDelete栈中出现过！
		$top=$result[$i];
	}
//栈顶指针为空时,说明递归结束,应该获得最后的执行列表!
	foreach($toExe as $temp_Exe)
	{
		$flag=1;
		/*
		 * 这个foreach解决的问题是对$toExe进行筛选得出最后的执行列表，由于结点被遍历的先后顺序问题，$toExe中可能有一部分应该删除的操作，现在再筛选一次！
		 */
		foreach($toDelete as $temp_delete)
		{
			if($temp_Exe->lAddrID==$temp_delete->lAddrID_2 && $temp_Exe->commandID==$temp_delete->commandID_2)
			$flag=0;
		}
		/*
		 * 这个foreach解决的是$Exe中操作可能重复的问题，即将去重！
		 */
		foreach($Exe as $check)
		{
			if($check->lAddrID==$temp_Exe->lAddrID && $check->commandID==$temp_Exe->commandID)
			$flag=0;
		}
		if($flag)
		$Exe[]=$temp_Exe;
	}
	return $Exe;
}
//finalExe();
//var_dump($toExe);
//var_dump($toDelete);
//var_dump($Exe);
?>