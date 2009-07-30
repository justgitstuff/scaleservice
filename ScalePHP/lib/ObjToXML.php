<?php
/**
 * Traverse an object to a XML node
 *
 * @param Object $obj -the original object
 * @return DomNode
 */
function buildXML($objArray)
{
	$domDoc=new DomDocument("1.0");
	$docRoot=$domDoc->createElement("root");
	if(is_Array($objArray))
	{
		$stateElem = $domDoc->createElement("actionReturn");
		$stateText=$domDoc->createTextNode("1");
		$stateElem->appendChild($stateText);
		foreach($objArray as $obj)
		{	
			$objRoot = $domDoc->createElement("row");
			foreach($obj as $key=>$value)
			{
				$newNode=$domDoc->createAttribute($key);
				$newNode->appendChild($domDoc->createTextNode($value));
				$objRoot->appendChild($newNode);
			}
			$docRoot->appendChild($objRoot);
		}
		$docRoot->appendChild($stateElem);
	}
	else
	{
		$stateElem = $domDoc->createElement("actionReturn");
		$stateText=$domDoc->createTextNode($objArray);
		$stateElem->appendChild($stateText);
		$docRoot->appendChild($stateElem);
	}
		
	$domDoc->appendChild($docRoot);
	echo $domDoc->saveXML();
}
function buildQuickTouch($objArray)
{
	$domDoc=new DomDocument("1.0");
	$docRoot=$domDoc->createElement("root");
	$rowsAttribute=$domDoc->createAttribute("rows");
	$rowsAttribute->appendChild($domDoc->createTextNode("3"));
	$columnsAttribute=$domDoc->createAttribute("columns");
	$columnsAttribute->appendChild($domDoc->createTextNode("10"));
	$docRoot->appendChild($rowsAttribute);
	$docRoot->appendChild($columnsAttribute);
	$itemsNode=$domDoc->createElement("items");
	$docRoot->appendChild($itemsNode);
	foreach($objArray as $obj)
	{	
		$objRoot = $domDoc->createElement("item");
		$str="";
		foreach($obj as $key=>$value)
		{
			$str=$str.$key.'='.$value.';';
		}
		//echo $str;
		$imageElem = $domDoc->createElement("image");
		$imageText=$domDoc->createAttribute("src");
		$imageText->appendChild($domDoc->createTextNode("asset/fractals/sample4.jpg"));
		$imageElem->appendChild($imageText);
		$objRoot->appendChild($imageElem);
		$descElem = $domDoc->createElement("description");
		$descText=$domDoc->createCDATASection($str);
		$descElem->appendChild($descText);
		$objRoot->appendChild($descElem);
		$itemsNode->appendChild($objRoot);
	}
	$domDoc->appendChild($docRoot);
	echo $domDoc->saveXML();
}
function buildHomeInfoXML($objArray)
{
	$domDoc=new DomDocument("1.0");
	$docRoot=$domDoc->createElement("root");
	$rowsAttribute=$domDoc->createAttribute("rows");
	$rowsAttribute->appendChild($domDoc->createTextNode("3"));
	$columnsAttribute=$domDoc->createAttribute("columns");
	$columnsAttribute->appendChild($domDoc->createTextNode("10"));
	$docRoot->appendChild($rowsAttribute);
	$docRoot->appendChild($columnsAttribute);
	$itemsNode=$domDoc->createElement("items");
	$docRoot->appendChild($itemsNode);
	foreach($objArray as $obj)
	{	
		$objRoot = $domDoc->createElement("item");
		$str="<title>";
		$str=$str.$obj->typeName;
		$str=$str."</title>";
		$str=$str.$obj->value;
		$str=$str.$obj->unit;
		$str=$str." ";
		$str=$str.$obj->aTime;
		//echo $str;
		$imageElem = $domDoc->createElement("image");
		$imageText=$domDoc->createAttribute("src");
		$imageText->appendChild($domDoc->createTextNode("image/sensorDataType.png"));
		$imageElem->appendChild($imageText);
		$objRoot->appendChild($imageElem);
		$descElem = $domDoc->createElement("description");
		$descText=$domDoc->createCDATASection($str);
		$descElem->appendChild($descText);
		$objRoot->appendChild($descElem);
		$itemsNode->appendChild($objRoot);
	}
	$domDoc->appendChild($docRoot);
	echo $domDoc->saveXML();
}
?>