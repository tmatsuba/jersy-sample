package org.gside.jersy.sample.json;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

/**
 * CSVをJSONに変換するためのクラス
 * 
 * @author matsuba
 *
 * @param <T>
 */
public class JsonConverter<T> {
	
	private ColumnPositionMappingStrategy<T> strat;
	private CsvToBean<T> csv = new CsvToBean<T>();
    private ObjectWriter ow = new ObjectMapper().writer();
    
	public JsonConverter(Class<T> clazz, String[] columns) {
		 strat = new ColumnPositionMappingStrategy<T>();
		 strat.setType(clazz);
		 strat.setColumnMapping(columns);
	}

	
    /**
     * csvをjsonに変換する。
     * 
     * @param line
     * @return
     * @throws IOException
     */
    public String csvToJson(String line) throws IOException {
    	if (line == null || line.isEmpty())
    		return "";

        List<T> list = csv.parse(strat, new StringReader(line));
        String json = ow.writeValueAsString(list.get(0));

        return  json;
    }


}
