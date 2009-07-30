<?php
//$_POST["topic"]="ÓãÏãÈâË¿";
$topic=urlencode($_POST["topic"]);
$url="http://news.google.cn/news?pz=1&ned=ccn&hl=zh-CN&q=".$topic."&output=rss";
//echo $url;
$fp=fopen($url,"r");
if(!$fp)
{
	echo("CONNECTION_ERROR");
}
else
{
	while(!feof($fp))
	{
		echo(fread($fp,4096));
	}
}
?>