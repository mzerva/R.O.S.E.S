/*STATS*/
DROP TABLE IF EXISTS `Stats` ;
CREATE TABLE `Stats` (
  `tableId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `duration` int(11) NOT NULL,
  `birth` int(11) NOT NULL,
  `death` int(11) DEFAULT NULL,
  `changes` int(11) NOT NULL,
  `sStart` int(11) NOT NULL,
  `sEnd` int(11) NOT NULL,
  `avgS` double NOT NULL,
  PRIMARY KEY (`tableId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `Stats` WRITE ;
UNLOCK TABLES ;

/*ATTRIBUTES*/
DROP TABLE IF EXISTS `Attributes` ;
CREATE TABLE `Attributes` (
  `aTableId` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`aTableId`, `version`),
  CONSTRAINT `btableId` FOREIGN KEY (`aTableId`) REFERENCES `Stats` (`tableId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `Attributes` WRITE ;
UNLOCK TABLES ;

/*TYPESOFCHANGES*/
DROP TABLE IF EXISTS `TypesOfChanges` ;
CREATE TABLE `TypesOfChanges` (
  `tCId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`tCId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `TypesOfChanges` WRITE ;
UNLOCK TABLES ;

INSERT INTO TypesOfChanges VALUES (1, 'deletion') ;
INSERT INTO TypesOfChanges VALUES (2, 'insertion') ;
INSERT INTO TypesOfChanges VALUES (3, 'keyChange') ;
INSERT INTO TypesOfChanges VALUES (4, 'TypeChange') ;

/*CHANGES*/
DROP TABLE IF EXISTS `Changes` ;
CREATE TABLE `Changes` (
  `cTableId` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `typeOfChange` int(11) NOT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`cTableId`, `version`, `typeOfChange`),
  KEY `typeChange_idx` (`typeOfChange`),
  CONSTRAINT `tableId` FOREIGN KEY (`cTableId`) REFERENCES `Stats` (`tableId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `typeChange` FOREIGN KEY (`typeOfChange`) REFERENCES `TypesOfChanges` (`tCId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `Changes` WRITE ;
UNLOCK TABLES ;

/*TRANSITIONS*/
DROP TABLE IF EXISTS `Transitions` ;
CREATE TABLE `Transitions` (
  `trId` int(11) NOT NULL,
  `oldVersion` varchar(40) NOT NULL,
  `newVersion` varchar(40) NOT NULL,
  PRIMARY KEY (`trId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `Transitions` WRITE ;
UNLOCK TABLES ;

/*TRANSITIONEVENTS*/
DROP TABLE IF EXISTS `TransitionEvents` ;
CREATE TABLE `TransitionEvents` (
  `tTrId` int(11) NOT NULL,
  `tableName` varchar(45) NOT NULL,
  `eventType` varchar(45) NOT NULL,
  `attrName` varchar(45) NOT NULL,
  `attrType` varchar(45) NOT NULL,
  `isKey` varchar(45) NOT NULL,
  `pKey` int(11) NOT NULL,
  `fKey` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`tTrId`,`tableName`,`eventType`,`attrName`),
  CONSTRAINT `btrId` FOREIGN KEY (`tTrId`) REFERENCES `Transitions` (`trId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `TransitionEvents` WRITE ;
UNLOCK TABLES ;

/*TYPESOFMETRICS*/
DROP TABLE IF EXISTS `TypesOfMetrics` ;
CREATE TABLE `TypesOfMetrics` (
  `tMId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`tMId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `TypesOfMetrics` WRITE ;
UNLOCK TABLES ;

INSERT INTO TypesOfMetrics VALUES (1, '#oldT') ;
INSERT INTO TypesOfMetrics VALUES (2, '#newT') ;
INSERT INTO TypesOfMetrics VALUES (3, '#oldA') ;
INSERT INTO TypesOfMetrics VALUES (4, '#newA') ;
INSERT INTO TypesOfMetrics VALUES (5, 'tIns') ;
INSERT INTO TypesOfMetrics VALUES (6, 'tDel') ;
INSERT INTO TypesOfMetrics VALUES (7, 'aIns') ;
INSERT INTO TypesOfMetrics VALUES (8, 'aDel') ;
INSERT INTO TypesOfMetrics VALUES (9, 'aTypeAlt') ;
INSERT INTO TypesOfMetrics VALUES (10, 'keyAlt') ;
INSERT INTO TypesOfMetrics VALUES (11, 'aTabIns') ;
INSERT INTO TypesOfMetrics VALUES (12, 'aTabDel') ;


/*METRICS*/
DROP TABLE IF EXISTS `Metrics` ;
CREATE TABLE `Metrics` (
  `mTrId` int(11) NOT NULL,
  `time` bigint(20) NOT NULL,
  `typeOfMetric` int(11) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`mTrId`,`typeOfMetric`),
  KEY `typeMetric_idx` (`typeOfMetric`),
  CONSTRAINT `trId` FOREIGN KEY (`mTrId`) REFERENCES `Transitions` (`trId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `typeMetric` FOREIGN KEY (`typeOfMetric`) REFERENCES `TypesOfMetrics` (`tMId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `Metrics` WRITE ;
UNLOCK TABLES ;

/*TYPESOFLIFESTATE*/
DROP TABLE IF EXISTS `TypesOfLifeState` ;
CREATE TABLE `TypesOfLifeState` (
  `tLFId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`tLFId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `TypesOfLifeState` WRITE ;
UNLOCK TABLES ;

INSERT INTO TypesOfLifeState VALUES (10, 'Dead') ;
INSERT INTO TypesOfLifeState VALUES (20, 'Survived') ;

/*TYPESOFACTIVITY*/
DROP TABLE IF EXISTS `TypesOfActivity` ;
CREATE TABLE `TypesOfActivity` (
  `tAId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`tAId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `TypesOfActivity` WRITE ;
UNLOCK TABLES ;

INSERT INTO TypesOfActivity VALUES (0, 'Rigid') ;
INSERT INTO TypesOfActivity VALUES (1, 'Quiet') ;
INSERT INTO TypesOfActivity VALUES (2, 'Active') ;

/*TYPESOFCLASS*/
DROP TABLE IF EXISTS `TypesOfClass` ;
CREATE TABLE `TypesOfClass` (
  `tOCId` int(11) NOT NULL ,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`tOCId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;

LOCK TABLES `TypesOfClass` WRITE ;
UNLOCK TABLES ;

INSERT INTO TypesOfClass VALUES (10, 'Sudden Death') ;
INSERT INTO TypesOfClass VALUES (11, 'Quiet, Dead') ;
INSERT INTO TypesOfClass VALUES (12, 'Active, Dead') ;
INSERT INTO TypesOfClass VALUES (20, 'Rigid') ;
INSERT INTO TypesOfClass VALUES (21, 'Quiet Survivor') ;
INSERT INTO TypesOfClass VALUES (22, 'Active Survivor') ;

/*sum(Changes)*/
create view SumChanges as select cTableId, sum(value) as sumUpdates from Changes group by cTableId ;

/*UpdatesPerVersion*/
create view UpdatesPerVersion as select cTableId, version, sum(value) as sumUpdates from Changes group by cTableId, version ;

/*count(Changes)*/
create view CountChanges as select cTableId, count(sumUpdates) as countUpdates from UpdatesPerVersion where sumUpdates>0 group by cTableId union select cTableId, 0 as countUpdates from SumChanges where sumUpdates=0 group by cTableId ;

/*ATU*/
create view ATUValues as select SC.cTableId, SC.sumUpdates/S.duration as ATU from SumChanges as SC, Stats as S where S.tableId=SC.cTableId group by SC.cTableId, ATU ;

/*Update Rate*/
create view UpdateRate as select C.cTableId, C.countUpdates/S.duration as updateRate from CountChanges as C, Stats as S where S.tableId=C.cTableId group by C.cTableId, updateRate ;

/*Size Scale Up*/
create view SchemaScaleUp as select tableId, sEnd/sStart as ScaleUp from Stats group by tableId ;

/*Average Update Volume*/
create view AverageUpdateVolume as select S.cTableId, S.sumUpdates/C.countUpdates as avgUpdVolume from SumChanges as S, CountChanges as C where S.cTableId=C.cTableId and C.countUpdates<>0 group by S.cTableId, avgUpdVolume union select S.cTableId, " " as avgUpdVolume from SumChanges as S , CountChanges as C where S.cTableId=C.cTableId and C.countUpdates=0 group by S.cTableId, avgUpdVolume ;

/*Life State*/
create view LifeState as  select tableId, 20 as Dead_Survived from Stats where death=0 group by tableId union select tableId, 10 as Dead_Survived from Stats where death<>0 group by tableId ;

/*Activity*/
create view Activity as select S.tableId , 2 as ActivityValue from Stats as S, ATUValues as AT where S.changes>5 and AT.ATU>0.1 and S.tableId=AT.cTableId group by S.tableId union select S.tableId, 0 as Activity from Stats as S where S.changes=0 group by S.tableId union select S.tableId, 1 as Activity from Stats as S where S.changes>0 and S.changes<6 group by S.tableId union select S.tableId, 1 as Activity from Stats as S, ATUValues as AT where S.changes>5 and AT.ATU<=0.1 and S.tableId=AT.cTableId group by S.tableId ;

/*Class*/
create view Class as select L.tableId, L.Dead_Survived+A.ActivityValue as ClassValue from LifeState as L, Activity as A where L.tableId=A.tableId group by L.tableId, ClassValue ;

/*All Stats*/
create view AllStats as select S.tableId, S.name, S.duration, S.birth, S.death, S.changes, S.sStart, S.sEnd, S.avgS, AT.ATU, AV.avgUpdVolume, C.countUpdates, SC.ScaleUp, U.updateRate, L.Dead_Survived, A.ActivityValue, Cl.ClassValue from Stats as S, ATUValues as AT, AverageUpdateVolume as AV, CountChanges as C, SchemaScaleUp as SC, UpdateRate as U , LifeState as L , Activity as A, Class as Cl where S.tableId=AT.cTableId and S.tableId=AV.cTableId and S.tableId=C.cTableId and S.tableId=SC.tableId and S.tableId=U.cTableId and S.tableId=L.tableId and S.tableId= A.tableId and S.tableId=Cl.tableId ;
