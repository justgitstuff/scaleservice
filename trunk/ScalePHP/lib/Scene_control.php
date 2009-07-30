<?php
class Scene_control
{
	public $sceneID,$lAddrID,$commandID;
	public function Scene_control()
	{
		
	}
	public static function addScene_control($sceneID,$lAddrID,$commandID)
	{
		global $db;
		$addFormat="call addScene_control(%d,'%s','%s')";
		$query=sprintf($addFormat,$sceneID,$lAddrID,$commandID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function getSceneControls($sceneID)
	{
		global $db;
		$selFormat='SELECT * from scene_control_info WHERE sceneID=%d';
		$query=sprintf($selFormat,$sceneID);
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
	public static function deleteScene_control($scene_controlID)
	{
		global $db;
		$delFormat="call deleteScene_control(%d)";
		$query=sprintf($delFormat,$scene_controlID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
}
?>