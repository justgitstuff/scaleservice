<?php
//the Local SQL Server
define("DB_ADDRESS","127.0.0.1");
//define("DB_ADDRESS","192.168.1.104");
define("DB_USERNAME","root");
define("DB_PASSWORD","");
define("DB_NAME","scaledb");
date_default_timezone_set("Asia/Shanghai");
$db=mysql_connect(DB_ADDRESS,DB_USERNAME,DB_PASSWORD);
mysql_select_db(DB_NAME,$db);
mb_internal_encoding('utf-8');
?>
