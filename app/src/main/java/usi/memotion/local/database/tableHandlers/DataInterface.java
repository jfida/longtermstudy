package usi.memotion.local.database.tableHandlers;

import org.json.JSONException;

public interface DataInterface 
{

	public String toJSONString() throws JSONException;

	public String getDataType();
}
