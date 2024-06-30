package org.anemoi.framework;




import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;

import java.sql.Date;


public class Main {
    
    public static void main(String[] args){
        DateTimeConverter dateTimeConverter = new DateConverter();
        dateTimeConverter.setPattern("yyyy-MM-dd");
        ConvertUtils.register(dateTimeConverter, Date.class);

        System.out.println(ConvertUtils.convert("1995-05-15", Date.class));

    }
}