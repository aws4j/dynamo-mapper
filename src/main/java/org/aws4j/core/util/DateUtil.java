package org.aws4j.core.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.*;

public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    private static final String YEAR_MONTH_FORMAT = "yyyyMM";
	private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("JST");

	private static DateTimeFormatter ymdFomatter = new DateTimeFormatterBuilder()
			.appendPattern( DEFAULT_DATE_FORMAT )
			.toFormatter();

	// TODO Mock Delegate Support
	public static Date getCurrentDate() {

		return new Date();
	}

	public static DateTime toDateTime( String ymd ) {
		return DateTime.parse( ymd, ymdFomatter );
	}

    public static String toYearMonth ( DateTime d ){
        return d.toString(YEAR_MONTH_FORMAT);
    }

	public static List<String> getYmdList( String fromYmd, String toYmd ) {

		List<String> dateList = new ArrayList<String>();
		DateTime currentDateTime = toDateTime( fromYmd );
		DateTime endDateTime = toDateTime( toYmd );
		do {
			String ymd = currentDateTime.toString( ymdFomatter );
			dateList.add( ymd );
			currentDateTime = currentDateTime.plusDays( 1 );
		} while ( currentDateTime.isBefore( endDateTime ) || currentDateTime.isEqual( endDateTime ));

		return dateList;
	}

}