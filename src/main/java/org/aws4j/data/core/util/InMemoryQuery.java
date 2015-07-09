package org.aws4j.data.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.data.core.Comparison;
import org.aws4j.data.core.FilterCriteria;
import org.aws4j.data.core.Query;
import org.aws4j.data.core.SortCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryQuery {

	private static Logger log = LoggerFactory.getLogger( InMemoryQuery.class );


	private interface Comparer<T> {
		public int compareTo( T me, T other );
	}

	private static class StringComparer implements Comparer<String> {
		@Override
		public int compareTo( String me, String other ) {
			return me.compareTo( other );
		}
	}

	private static class IntegerComparer implements Comparer<Integer> {
		@Override
		public int compareTo( Integer me, Integer other ) {
			throw new NotImplementedException();
		}
	}

	private static class LongComparer implements Comparer<Long> {
		@Override
		public int compareTo( Long me, Long other ) {
			throw new NotImplementedException();
		}
	}

	private interface ConditionComparer {
		public boolean compare( Object obj, FilterCriteria condition );
	}

	private static class EqualComparer implements ConditionComparer {
		@Override
		public boolean compare( Object obj, FilterCriteria condition ) {

			/*
			for( Object value : condition.getValues() ) {
				Comparer comparer = comparers.get( value.getClass() );
				if( comparer.compareTo( obj, value ) != 0 ) {
					return false;
				}
			}
			*/
			return true;
		}
	}

	private static class GreaterEqualComparer implements ConditionComparer {
		@Override
		public boolean compare( Object obj, FilterCriteria condition ) {
			throw new NotImplementedException();
		}
	}

	private static class GreaterThanComparer implements ConditionComparer {
		@Override
		public boolean compare( Object obj, FilterCriteria condition ) {
			throw new NotImplementedException();
		}
	}

	private static class LesserEqualComparer implements ConditionComparer {
		@Override
		public boolean compare( Object obj, FilterCriteria condition ) {
			throw new NotImplementedException();
		}
	}

	private static class LesserThanComparer implements ConditionComparer {
		@Override
		public boolean compare( Object obj, FilterCriteria condition ) {
			throw new NotImplementedException();
		}
	}

	private static class BetweenComparer implements ConditionComparer {
		@Override
		public boolean compare( Object obj, FilterCriteria condition ) {
			return false;
		}
	}

	private static Map<Class<?>, Comparer<?>> comparers;

	private static Map<Comparison, ConditionComparer> conditionComparers;

	static {

		comparers = new HashMap<Class<?>, Comparer<?>>() {
			{
				put( String.class, new StringComparer() );
				put( Integer.class, new IntegerComparer() );
				put( Long.class, new LongComparer() );
			}
		};

		conditionComparers = new HashMap<Comparison, ConditionComparer>() {
			{
				put( Comparison.EQ, new EqualComparer() );
				put( Comparison.GE, new GreaterEqualComparer() );
				put( Comparison.GT, new GreaterThanComparer() );
				put( Comparison.LE, new LesserEqualComparer() );
				put( Comparison.LT, new LesserThanComparer() );
				put( Comparison.BETWEEN, new BetweenComparer() );
			}
		};
	}

	public <M> List<M> query( Collection<M> collection , Query<M> query ) {

		FilterCriteria<M> condition = query.getCondition();
		SortCriteria<M> sortCriteria = query.getSortCriteria();

		List<M> remains = new ArrayList<M>();
		for ( M model : collection ) {
			if ( isMatch( model, condition ) ) {
				remains.add( model );
			}
		}
		return sort( remains, sortCriteria );
	}

	private <M> boolean isMatch( M model, FilterCriteria<M> condition ) {

		/*
		for ( String name : query.getConditions().keySet() ) {

			Condition condition = query.getConditions().get( name );
			ConditionComparer comparer = conditionComparers.get( condition.getComparison() );
			if ( comparer == null ) {
				throw new UnsupportedOperationException();
			}
			if ( ! comparer.compare( ReflectUtil.getPropertyValue( obj, name ), condition ) ) {
				return false;
			}
		}
		*/
		return true;
	}

	private <M> List<M> sort( List<M> remains, SortCriteria<M> sortCriteria ) {
		List<M> sortedList = new ArrayList<M>();

		return sortedList;
	}

}
