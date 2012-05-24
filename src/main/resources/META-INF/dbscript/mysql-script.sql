
    drop table if exists wf_activityinst;

    drop table if exists wf_participant;

    drop table if exists wf_primarykey;

    drop table if exists wf_processdefine;

    drop table if exists wf_processinst;
 
    drop table if exists wf_transctrl;

    drop table if exists wf_workitem;

    create table wf_activityinst (
        ACTIVITYINSTID bigint not null,
        ACTIVITYINSTNAME varchar(100),
        DESCRIPTION longtext,
        ACTIVITYTYPE varchar(10),
        ACTIVITYDEFID varchar(50),
        PROCESSINSTID bigint,
        CURRENTSTATE bigint,
        LIMITTIME bigint,
        CREATETIME datetime,
        STARTTIME datetime,
        ENDTIME datetime,
        FINALTIME datetime,
        primary key (ACTIVITYINSTID)
    );

    create table wf_participant (
        PARTICID bigint not null,
        WORKITEMID bigint not null,
        PARTICTYPE varchar(20),
        PARTICIPANT varchar(50),
        PARTICIPANT2 varchar(50),
        primary key (PARTICID)
    );

    create table wf_primarykey (
        CODE bigint not null,
        NAME varchar(30) not null,
        primary key (CODE, NAME)
    );

    create table wf_processdefine (
        PROCESSDEFID bigint not null,
        PROCESSDEFNAME varchar(100),
        PROCESSCHNAME varchar(100),
        DESCRIPTION longtext,
        CURRENTSTATE integer,
        VERSIONSIGN varchar(20),
        LIMITTIME integer,
        PROCESSDEFCONTENT longtext,
        CREATETIME datetime,
        CREATOR varchar(64),
        UPDATETIME datetime,
        UPDATOR varchar(64),
        primary key (PROCESSDEFID)
    );

    create table wf_processinst (
        PROCESSINSTID bigint not null,
        PROCESSDEFID bigint,
        PROCESSINSTNAME varchar(100),
        CREATOR varchar(50),
        CURRENTSTATE bigint,
        SUBFLOW varchar(1),
        LIMITNUM bigint,
        CREATETIME datetime,
        STARTTIME datetime,
        ENDTIME datetime,
        FINALTIME datetime,
        MAINPROCINSTID bigint,
        PARENTPROCINSTID bigint,
        ACTIVITYINSTID bigint,
        primary key (PROCESSINSTID)
    );

    create table wf_transctrl (
        TRANSCTRLID bigint not null,
        SRCACTDEFID varchar(50),
        SRCACTDEFNAME varchar(100),
        DESTACTTYPE varchar(10),
        DESTACTDEFID varchar(50),
        DESTACTDEFNAME varchar(100),
        SRCACTTYPE varchar(10),
        TRANSTIME datetime,
        PROCESSINSTID bigint,
        ISUSE varchar(1),
        ISSTARTDESTACT varchar(1),
        primary key (TRANSCTRLID)
    );

    create table wf_workitem (
        WORKITEMID bigint not null,
        WORKITEMNAME varchar(100),
        WORKITEMTYPE varchar(10),
        CURRENTSTATE bigint,
        PARTICIPANT varchar(200),
        LIMITTIME bigint,
        ACTIVITYINSTID bigint,
        PROCESSINSTID bigint,
        BIZSTATE bigint,
        STARTTIME datetime,
        ENDTIME datetime,
        FINALTIME datetime,
        activityDefId varchar(50),
        primary key (WORKITEMID)
    );
