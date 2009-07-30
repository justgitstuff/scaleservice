<?php
class Scene
{
	public $sceneID,$sceneName,$keyWord;
	public function Scene()
	{
		
	}
	public static function getScenes()
	{
		global $db;
		$selFormat='SELECT * from scene';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function enterScene($sceneID)
	{
		global $db;
		$addFormat="call enterScene(%d)";
		$query=sprintf($addFormat,$sceneID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return SERVER_BUSY;
	}
	public static function addScene($sceneName,$keyWord,$advMin)
	{
		global $db;
		$addFormat="call addScene('%s','%s',%d)";
		$query=sprintf($addFormat,$sceneName,$keyWord,$advMin);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function modifyScene($sceneID,$sceneName,$keyWord,$advMin)
	{
		global $db;
		$uptFormat="call modifyScene(%d,'%s','%s',%d)";
		$query=sprintf($uptFormat,$sceneID,$sceneName,$keyWord,$advMin);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function deleteScene($sceneID)
	{
		global $db;
		$delFormat='call deleteScene(%d)';
		$query=sprintf($delFormat,$sceneID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
}
?>