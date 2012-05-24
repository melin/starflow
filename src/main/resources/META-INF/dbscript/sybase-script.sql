
    drop table wf_activityinst;

    drop table wf_participant;

    drop table wf_primarykey;
 
    drop table wf_processdefine;

    drop table wf_processinst;

    drop table wf_transctrl;

    drop table wf_workitem;

    create table wf_activityinst (
        ACTIVITYINSTID numeric(19,0) not null,
        ACTIVITYINSTNAME varchar(100) null,
        DESCRIPTION varchar(500) null,
        ACTIVITYTYPE varchar(10) null,
        ACTIVITYDEFID varchar(50) null,
        PROCESSINSTID numeric(19,0) null,
        CURRENTSTATE numeric(19,0) null,
        LIMITTIME numeric(19,0) null,
        CREATETIME datetime null,
        STARTTIME datetime null,
        ENDTIME datetime null,
        FINALTIME datetime null,
        primary key (ACTIVITYINSTID)
    );

    create table wf_participant (
        PARTICID numeric(19,0) not null,
        WORKITEMID numeric(19,0) not null,
        PARTICTYPE varchar(20) null,
        PARTICIPANT varchar(50) null,
        PARTICIPANT2 varchar(50) null,
        primary key (PARTICID)
    );

    create table wf_primarykey (
        CODE numeric(19,0) not null,
        NAME varchar(30) not null,
        primary key (CODE, NAME)
    );

    create table wf_processdefine (
        PROCESSDEFID numeric(19,0) not null,
        PROCESSDEFNAME varchar(100) null,
        PROCESSCHNAME varchar(100) null,
        DESCRIPTION varchar(500) null,
        CURRENTSTATE int null,
        VERSIONSIGN varchar(20) null,
        LIMITTIME int null,
        PROCESSDEFCONTENT varchar(65535) null,
        CREATETIME datetime null,
        CREATOR varchar(64) null,
        UPDATETIME datetime null,
        UPDATOR varchar(64) null,
        primary key (PROCESSDEFID)
    );

    create table wf_processinst (
        PROCESSINSTID numeric(19,0) not null,
        PROCESSDEFID numeric(19,0) null,
        PROCESSINSTNAME varchar(100) null,
        CREATOR varchar(50) null,
        CURRENTSTATE numeric(19,0) null,
        SUBFLOW varchar(1) null,
        LIMITNUM numeric(19,0) null,
        CREATETIME datetime null,
        STARTTIME datetime null,
        ENDTIME datetime null,
        FINALTIME datetime null,
        MAINPROCINSTID numeric(19,0) null,
        PARENTPROCINSTID numeric(19,0) null,
        ACTIVITYINSTID numeric(19,0) null,
        primary key (PROCESSINSTID)
    );

    create table wf_transctrl (
        TRANSCTRLID numeric(19,0) not null,
        SRCACTDEFID varchar(50) null,
        SRCACTDEFNAME varchar(100) null,
        DESTACTTYPE varchar(10) null,
        DESTACTDEFID varchar(50) null,
        DESTACTDEFNAME varchar(100) null,
        SRCACTTYPE varchar(10) null,
        TRANSTIME datetime null,
        PROCESSINSTID numeric(19,0) null,
        ISUSE varchar(1) null,
        ISSTARTDESTACT varchar(1) null,
        primary key (TRANSCTRLID)
    );

    create table wf_workitem (
        WORKITEMID numeric(19,0) not null,
        WORKITEMNAME varchar(100) null,
        WORKITEMTYPE varchar(10) null,
        CURRENTSTATE numeric(19,0) null,
        PARTICIPANT varchar(200) null,
        LIMITTIME numeric(19,0) null,
        ACTIVITYINSTID numeric(19,0) null,
        PROCESSINSTID numeric(19,0) null,
        BIZSTATE numeric(19,0) null,
        STARTTIME datetime null,
        ENDTIME datetime null,
        FINALTIME datetime null,
        activityDefId varchar(50) null,
        primary key (WORKITEMID)
    );
