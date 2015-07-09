package org.aws4j.data.dynamo.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aws4j.core.exception.NotImplementedException;
import org.eclipse.persistence.internal.jpa.deployment.SEPersistenceUnitInfo;
import org.eclipse.persistence.internal.jpa.metadata.MetadataLogger;
import org.eclipse.persistence.internal.jpa.metadata.MetadataProject;
import org.eclipse.persistence.internal.jpa.metadata.accessors.classes.ConverterAccessor;
import org.eclipse.persistence.internal.jpa.metadata.accessors.classes.EmbeddableAccessor;
import org.eclipse.persistence.internal.jpa.metadata.accessors.classes.EntityAccessor;
import org.eclipse.persistence.internal.jpa.metadata.accessors.classes.MappedSuperclassAccessor;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataAnnotation;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataAsmFactory;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataClass;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataFactory;
import org.eclipse.persistence.internal.jpa.metadata.columns.TenantDiscriminatorColumnMetadata;
import org.eclipse.persistence.internal.jpa.metadata.converters.ConverterMetadata;
import org.eclipse.persistence.internal.jpa.metadata.converters.MixedConverterMetadata;
import org.eclipse.persistence.internal.jpa.metadata.converters.ObjectTypeConverterMetadata;
import org.eclipse.persistence.internal.jpa.metadata.converters.SerializedConverterMetadata;
import org.eclipse.persistence.internal.jpa.metadata.converters.StructConverterMetadata;
import org.eclipse.persistence.internal.jpa.metadata.converters.TypeConverterMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.HashPartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.PartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.PinnedPartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.RangePartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.ReplicationPartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.RoundRobinPartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.UnionPartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.ValuePartitioningMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.NamedNativeQueryMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.NamedPLSQLStoredFunctionQueryMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.NamedPLSQLStoredProcedureQueryMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.NamedQueryMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.NamedStoredFunctionQueryMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.NamedStoredProcedureQueryMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.OracleArrayTypeMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.OracleObjectTypeMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.PLSQLRecordMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.PLSQLTableMetadata;
import org.eclipse.persistence.internal.jpa.metadata.queries.SQLResultSetMappingMetadata;
import org.eclipse.persistence.internal.jpa.metadata.sequencing.SequenceGeneratorMetadata;
import org.eclipse.persistence.internal.jpa.metadata.sequencing.TableGeneratorMetadata;
import org.eclipse.persistence.internal.jpa.metadata.sequencing.UuidGeneratorMetadata;
import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappings;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataSource implements org.eclipse.persistence.jpa.metadata.MetadataSource {

	private static final Logger Log = LoggerFactory.getLogger( MetadataSource.class );

    @Override
	public Map<String, Object> getPropertyOverrides( Map<String, Object> properties, ClassLoader classLoader, SessionLog log ) {
		Log.debug( classLoader.getClass().toString() );
		return properties;
	}

	@Override
	public XMLEntityMappings getEntityMappings( Map<String, Object> properties, ClassLoader classLoader, SessionLog log ) {
		return new EntityMappingsConstructor( properties, classLoader, log ).constructXMLEntiryMappings();
	}



	private static class EntityMappingsConstructor {

		private Map<String, Object> properties;
		private ClassLoader classLoader;
		private SessionLog log;
		private ServerSession session;

		EntityMappingsConstructor( Map<String, Object> properties, ClassLoader classLoader, SessionLog log ) {
			this.properties = properties;
			this.classLoader = classLoader;
			this.log = log;
			this.session = (ServerSession) log.getSession();
		}

		private List<EntityAccessor> constructEntityAccessors() {
			List<EntityAccessor> entities = new ArrayList<EntityAccessor>();
			entities.add( createEntityAccessor() );
			return entities;
		}

		private EntityAccessor createEntityAccessor() {
			MetadataFactory metaFactory = new MetadataAsmFactory(new MetadataLogger(session), classLoader);
			MetadataAnnotation metaAnnotation = new MetadataAnnotation();
			MetadataClass metaClass = new MetadataClass( metaFactory, "TestModel1" );
			MetadataProject metaProject = new MetadataProject( new SEPersistenceUnitInfo(), session, false, false, false, false, false );

			return new EntityAccessor( metaAnnotation, metaClass, metaProject );
		}

		public XMLEntityMappings constructXMLEntiryMappings() {

			XMLEntityMappings xml = new XMLEntityMappings();

			xml.setConverters(new ArrayList<ConverterMetadata>());
	        xml.setConverterAccessors(new ArrayList<ConverterAccessor>());
	        xml.setEmbeddables(new ArrayList<EmbeddableAccessor>());
	        xml.setHashPartitioning(new ArrayList<HashPartitioningMetadata>());
	        xml.setMappedSuperclasses(new ArrayList<MappedSuperclassAccessor>());
	        xml.setMixedConverters(new ArrayList<MixedConverterMetadata>());
	        xml.setNamedQueries(new ArrayList<NamedQueryMetadata>());
	        xml.setNamedStoredFunctionQueries(new ArrayList<NamedStoredFunctionQueryMetadata>());
	        xml.setNamedNativeQueries(new ArrayList<NamedNativeQueryMetadata>());
	        xml.setNamedPLSQLStoredFunctionQueries(new ArrayList<NamedPLSQLStoredFunctionQueryMetadata>());
	        xml.setNamedPLSQLStoredProcedureQueries(new ArrayList<NamedPLSQLStoredProcedureQueryMetadata>());
	        xml.setNamedStoredProcedureQueries(new ArrayList<NamedStoredProcedureQueryMetadata>());
	        xml.setObjectTypeConverters(new ArrayList<ObjectTypeConverterMetadata>());
	        xml.setOracleArrayTypes(new ArrayList<OracleArrayTypeMetadata>());
	        xml.setOracleObjectTypes(new ArrayList<OracleObjectTypeMetadata>());
	        xml.setPartitioning(new ArrayList<PartitioningMetadata>());
	        xml.setPinnedPartitioning(new ArrayList<PinnedPartitioningMetadata>());
	        xml.setPLSQLRecords(new ArrayList<PLSQLRecordMetadata>());
	        xml.setPLSQLTables(new ArrayList<PLSQLTableMetadata>());
	        xml.setRangePartitioning(new ArrayList<RangePartitioningMetadata>());
	        xml.setReplicationPartitioning(new ArrayList<ReplicationPartitioningMetadata>());
	        xml.setRoundRobinPartitioning(new ArrayList<RoundRobinPartitioningMetadata>());
	        xml.setSequenceGenerators(new ArrayList<SequenceGeneratorMetadata>());
	        xml.setSerializedConverters(new ArrayList<SerializedConverterMetadata>()); // TODO: add to config
	        xml.setSqlResultSetMappings(new ArrayList<SQLResultSetMappingMetadata>());
	        xml.setStructConverters(new ArrayList<StructConverterMetadata>());
	        xml.setTableGenerators(new ArrayList<TableGeneratorMetadata>());
	        xml.setTenantDiscriminatorColumns(new ArrayList<TenantDiscriminatorColumnMetadata>());
	        xml.setTypeConverters(new ArrayList<TypeConverterMetadata>());
	        xml.setUnionPartitioning(new ArrayList<UnionPartitioningMetadata>());
	        xml.setUuidGenerators(new ArrayList<UuidGeneratorMetadata>());
	        xml.setValuePartitioning(new ArrayList<ValuePartitioningMetadata>());

			xml.setEntities( constructEntityAccessors() );
			return xml;
		}
	}
}
