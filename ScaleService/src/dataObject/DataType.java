package dataObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import util.Direction;

import com.google.appengine.api.datastore.Key;

import exception.DataTypeException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class DataType
{
	@SuppressWarnings("unchecked")
	public static DataType getDataType(String typeName)
	{
		DataType returnDataType=null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(DataType.class);
		query.setFilter("typeName == tn");
		query.declareParameters("String tn");
		try
		{
			List<DataType> results = (List<DataType>) query.execute(typeName);
			if (results.iterator().hasNext())
			{
				returnDataType = results.iterator().next();
			}
		}finally
		{
			//noting to do
		}
		return returnDataType;
	}
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key dataTypeID;
	@Persistent
	private String unit;
	@Persistent
	private String typeName;
	@Persistent
	private Double maxCustom;
	@Persistent
	private Double minCustom;
	@Persistent
	private List<SensorData> sensorData;
	/**
	 * @param unit
	 * @param typeName
	 * @param maxCustom
	 * @param minCustom
	 */
	public DataType(String unit, String typeName, Double maxCustom,
			Double minCustom)
	{
		super();
		this.unit = unit;
		this.typeName = typeName;
		this.maxCustom = maxCustom;
		this.minCustom = minCustom;
		this.sensorData=new ArrayList<SensorData>();
	}
	public void addSensorData(SensorData sensorData)
	{
		this.sensorData.add(sensorData);
	}
	
	/**
	 * @return the sensorData
	 */
	public List<SensorData> getSensorData()
	{
		return sensorData;
	}
	public void saveAsNew() throws DataTypeException
	{
		if(DataType.getDataType(this.typeName)==null && this.dataTypeID==null)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try
			{
				pm.makePersistent(this);
			} finally
			{
				pm.close();
			}
		}
		else if(this.dataTypeID!=null)
		{
			throw new DataTypeException(DataTypeException.PrimaryKeyNotNull);
		}
		else
		{
			throw new DataTypeException(DataTypeException.TypeNameAlreadyExist);
		}
	}
	/**
	 * @return the dataTypeID
	 */
	public Key getDataTypeID()
	{
		return dataTypeID;
	}
	/**
	 * @return the unit
	 */
	public String getUnit()
	{
		return unit;
	}
	/**
	 * @return the typeName
	 */
	public String getTypeName()
	{
		return typeName;
	}
	/**
	 * @return the maxCustom
	 */
	public Double getMaxCustom()
	{
		return maxCustom;
	}
	/**
	 * @return the minCustom
	 */
	public Double getMinCustom()
	{
		return minCustom;
	}
	/**
	 * @param dataTypeID the dataTypeID to set
	 */
	public void setDataTypeID(Key dataTypeID)
	{
		this.dataTypeID = dataTypeID;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	/**
	 * @param maxCustom the maxCustom to set
	 */
	public void setMaxCustom(Double maxCustom)
	{
		this.maxCustom = maxCustom;
	}
	/**
	 * @param minCustom the minCustom to set
	 */
	public void setMinCustom(Double minCustom)
	{
		this.minCustom = minCustom;
	}
	public boolean equals(DataType d)
	{
		if(d.typeName.equals(this.typeName))
			return true;
		else
			return false;
	}
	/**
	 * 查找当前数据类型的值是否符合要求，如果不符合要求的话则返回相应的解决方案
	 * @return List<Operation>
	 */
	@SuppressWarnings("unchecked")
	public List<Operation> findToDo()
	{
		SensorData lastSensorData=getLastSensorData();
		List<Operation> todo=new ArrayList<Operation>();
		Operation iOperation;
		if(lastSensorData.getValue()>this.maxCustom)//如果当前值超过了预定值
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(Operation.class);
			try
			{
				List<Operation> results = (List<Operation>) query.execute();
				Iterator<Operation> it=results.iterator();
				while(it.hasNext())
				{
					iOperation=it.next();
					if(iOperation.getDataType().equals(this) && iOperation.getDirection().equals(Direction.Down))
					{
						todo.add(iOperation);
					}
				}
			}finally
			{
				//noting to do
			}
		}
		else if(lastSensorData.getValue()<this.minCustom)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(Operation.class);
			try
			{
				List<Operation> results = (List<Operation>) query.execute();
				Iterator<Operation> it=results.iterator();
				while(it.hasNext())
				{
					iOperation=it.next();
					if(iOperation.getDataType().equals(this) && iOperation.getDirection().equals(Direction.Up))
					{
						todo.add(iOperation);
					}
				}
			}finally
			{
				//noting to do
			}
		}
		return todo;
	}
	/**
	 * 获取当前数据类型的最后一次数据
	 * @return SensorData
	 */
	public SensorData getLastSensorData()
	{
		Iterator<SensorData> it=sensorData.iterator();
		SensorData lastSensorData=null;
		while(it.hasNext())
		{
			lastSensorData=it.next();
		}
		return lastSensorData;
	}
}
