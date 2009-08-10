<?php
# FileName="Connection_php_mysql.htm"
# Type="MYSQL"
# HTTP="true"
$hostname_LocalServer = "127.0.0.1";
$database_LocalServer = "scaledb";
$username_LocalServer = "root";
$password_LocalServer = "";
$LocalServer = mysql_pconnect($hostname_LocalServer, $username_LocalServer, $password_LocalServer) or trigger_error(mysql_error(),E_USER_ERROR); 
?>