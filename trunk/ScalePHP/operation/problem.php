<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
$toDelete=array();//��Ҫɾ���������б�
$toExe=array();//��δ��ɸѡ��Ҫִ�еĲ����б�
$Exe=array();//��ɸѡ֮��Ĳ����б�
//����temp_operation,�����������Ĳ�����
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
 * ����ǹ�����ͼ�еĽ�㣬ÿ��������������ݣ���$todo,$list_accroding,$list_contrary.$todo��һ��operation��ʵ�����Ժ���$toExe
 * ջ��$list_accroding��һ��temp_operationʵ�����飬���������ڵ�ǰ���ֱ���������Ĳ�����$list_contraryҲ��һ��temp_operation��ʵ�����飬����
 * �����뵱ǰ���ֱ�ӷ������Ĳ���.
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
 * ����������ѵĺ���,������ÿ�������������������,���Ѵ���֮����صĲ���,������Щ������$contraryջ.
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
			 * �ڽ�������������������������$toDeleteջ֮ǰ,Ҫ�ȼ��������������Ƿ��Ѿ���������$toDeleteջ��!
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
//����getList����,���õ���ʼջ��Ҳ�ǽ���ɸѡ�ķ�Χ.Ҫ��ջ��Ԫ��λ�������β��,ջ�Ķ���.
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
	$result=getList();//����getList����,�õ���ʼջ.
	$i=count($result)-1;//����$i,�õ�ջ����ָ��!
	$top=$result[$i];//ջ��Ԫ�س�ջ��
	while($top)
	{
		/*
		 * һ��Ҫ�ж�$top����lAddrID��commandID����lAddrID_2��commandID_2,���������ɽ��ʱ���ݲ����ķ�ʽ��ͬ!
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
		$i=$i-1;//ջ��Ԫ��Ҫ��ջ,��Ҫ����ԭ����ջ��Ԫ��unset��,���ҽ�ջ��ָ������һ������������Ҫ�����ɽ��֮�����!
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
		}//����ʵ�ֵ��ǽ��뵱ǰ����෴�����Ĳ�����������������$toDeleteջ,����Ҫɾ���Ĳ����б����⵽$temp_contrary�е�Ԫ��������$lAddrID_2��$commandID_2!!!ǧ��ҪŪ����,�����ò���!!!
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
					//array_push($result,$temp_according);//����ʹ�õ�ʱ�������⣡���������ֱ�������ղű�unset�����±ֱ꣬�Ӹ���һ������Ԫ�ظ�ֵ������
					$result[$i+1]=$temp_according;
					$i=$i+1;
				}
			}
		}//����ʵ�ֵ��ǽ�ջ��Ԫ�ص�������������ջ!����ջ֮ǰ��Ҫ�ȼ��ò����Ƿ���$toDeleteջ�г��ֹ�����������Ҫ���Ǽ��ò����Ƿ���ִ���б�$toExe�г��ֹ�������������ѭ����
		/*
		 * ����Ҫ����㱾��Ĳ���������б�ջ$toExe����ջ֮ǰ�ȼ�����Ƿ���Ҫɾ���Ĳ����б�$toDelete�г��ֹ���
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
		}//����ʵ�ֵ��ǽ���ջ��Ԫ�ؼ��뵽��Ҫִ�еĲ�����!����֮ǰҪ�ȼ��һ���������֪����$toDeleteջ�г��ֹ���
		$top=$result[$i];
	}
//ջ��ָ��Ϊ��ʱ,˵���ݹ����,Ӧ�û������ִ���б�!
	foreach($toExe as $temp_Exe)
	{
		$flag=1;
		/*
		 * ���foreach����������Ƕ�$toExe����ɸѡ�ó�����ִ���б����ڽ�㱻�������Ⱥ�˳�����⣬$toExe�п�����һ����Ӧ��ɾ���Ĳ�����������ɸѡһ�Σ�
		 */
		foreach($toDelete as $temp_delete)
		{
			if($temp_Exe->lAddrID==$temp_delete->lAddrID_2 && $temp_Exe->commandID==$temp_delete->commandID_2)
			$flag=0;
		}
		/*
		 * ���foreach�������$Exe�в��������ظ������⣬����ȥ�أ�
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