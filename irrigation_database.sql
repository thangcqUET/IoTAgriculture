create database if not exists irrigation_database;

use irrigation_database;

CREATE TABLE IF NOT EXISTS Users (
    UserID INT NOT NULL AUTO_INCREMENT,
    UserName VARCHAR(50) NOT NULL,
    UPassword VARCHAR(50) NOT NULL,
    PRIMARY KEY (UserID)
)  ENGINE INNODB DEFAULT CHARSET=UTF8;

create table if not exists DeviceTypes(
	DeviceTypeID int not null auto_increment,
    DeviceType varchar(50) not null,
    primary key(DeviceTypeID)
) engine InnoDB default charset=utf8;

create table if not exists FarmTypes(
	FarmTypeID int not null auto_increment,
    FarmType varchar(50) not null,
    primary key(FarmTypeID)
) engine InnoDB default charset=utf8;

create table if not exists PlotTypes(
	PlotTypeID int not null auto_increment,
    PlotType varchar(50) not null,
    primary key(PlotTypeID)
) engine InnoDB default charset=utf8;

create table if not exists Locates(
	LocateID int not null,
    LocateName varchar(50),
    Lon float,
    Lat float,
    primary key(LocateID)
) engine InnoDB default charset=utf8;
create table if not exists Farms(
	FarmID int not null auto_increment,
    LocateID int,
    Area float8,
    FarmTypeID int,
    Status bool,
    UserID int,
    primary key(FarmID),
	foreign key(UserID) references Users(UserID)on delete restrict on update cascade,
	foreign key(FarmTypeID) references FarmTypes(FarmTypeID)on delete restrict on update cascade,
    foreign key(LocateID) references Locates(LocateID) on delete restrict on update cascade
) engine InnoDB default charset=utf8;

create table if not exists Plots(
	PlotID int not null auto_increment,
    Area float8,
    PlotTypeID int,
    FarmID int,
    primary key(PlotID),
    foreign key(FarmID) references Farms(FarmID)on delete restrict on update cascade,
	foreign key(PlotTypeID) references PlotTypes(PlotTypeID)on delete restrict on update cascade
) engine InnoDB default charset=utf8;

create table if not exists Devices(
	DeviceID bigint not null,
    DeviceTypeID int,
    DeviceName varchar(50),
    Status bool,
    PlotID int,
    primary key(DeviceID),
	foreign key(DeviceTypeID) references DeviceTypes(DeviceTypeID)on delete restrict on update cascade,
    foreign key(PlotID) references Plots(PlotID)on delete restrict on update cascade
) engine InnoDB default charset=utf8;

create table if not exists Sensing(
	SensingID bigint auto_increment,
	DeviceID bigint,
    PlotID int,
    SoilMoisture float4,
    Humidity float4,
    Temperature float4,
    SoilTemperature float4,
    LightLevel int,
    TimeOfMeasurement datetime,
    primary key(SensingID),
    foreign key(PlotID) references Plots(PlotID) on delete restrict on update cascade,
    foreign key(DeviceID) references Devices(DeviceID) on delete restrict on update cascade
) engine InnoDB default charset=utf8;

create table if not exists Forecast(
	ForecastID bigint auto_increment,
	DeviceID bigint,
    PlotID int,
    Precipitation float,
    Temperature float,
    Humidity float,
    ForecastStatus varchar(50),
    TimeOfMeasurement datetime,
    primary key(ForecastID),
    foreign key(PlotID) references Plots(PlotID)on delete restrict on update cascade,
    foreign key(DeviceID) references Devices(DeviceID) on delete restrict on update cascade
) engine InnoDB default charset=utf8;

create table if not exists Controlling(
	ControllingID bigint,
	DeviceID bigint,
    PlotID int,
    AmountOfWater float, -- l
    WateringDuration int, -- s
    TimeOfControl datetime,
    primary key(ControllingID),
    foreign key(DeviceID) references Devices(DeviceID)on delete restrict on update cascade,
    foreign key(PlotID) references Plots(PlotID)on delete restrict on update cascade
) engine InnoDB default charset=utf8;

update Farms set Status = true where FarmID = 5;
select * from Plots;
select * from Devices where PlotID in (select PlotID from Plots where FarmID = 1);
select * from Sensing;
Select * from Users where UserName = 'a' & UPassword = 'cc175b9c0f1b6a831c399e269772661';
Select * from Users where UserName = 'admin' and UPassword = '25d55ad283aa400af464c76d713c07ad';
insert into Farms(FarmType) values (1);
insert into Plots(Area, PlotTypeID, FarmID) values (null,null,null);
delete from Devices where 1;
-- alter table Farms auto_increment = 1;
-- delete from Locates;
-- insert into Locates(LocateID, locateName) values (1234,"thắng");
-- insert into Users(UserName,UPassword) values ("thangdeptrai","1");
-- insert into FarmTypes(FarmType) values ("Trong hanh");
-- insert into FarmTypes(FarmType) values ("Trong cai");
-- insert into Farms(Locate,Area,FarmTypeID,UserID) values ("G2-DHCN",5.0,1,1);
-- insert into DeviceTypes(DeviceType) values ("sensor");
-- insert into DeviceTypes(DeviceType) values ("controller");
-- 
-- insert into PlotTypes(PlotTypes.PlotType) values("Trong hanh");
-- insert into Plots(Plots.PlotID, Plots.FarmID, Plots.PlotTypeID, Plots.Area) values(1,1,1,12);
-- insert into Devices(Devices.DeviceID, Devices.DeviceName, Devices.DeviceTypeID, Devices.PlotID) values(1,"cam bien anh sang", 1,1);
-- 
-- insert into Devices(DeviceTypeID,DeviceName,DeviceFunction,FarmID) values (1,"Soil Moisture Sensor","Do do am dat",1);
-- insert into Devices(DeviceTypeID,DeviceName,DeviceFunction,FarmID) values (1,"Humidity Sensor","Do do am khong khi",1);
-- insert into Devices(DeviceTypeID,DeviceName,DeviceFunction,FarmID) values (1,"Temperature Sensor","Do nhiet do",1);
-- insert into Devices(DeviceTypeID,DeviceName,DeviceFunction,FarmID) values (2,"Pump","Bom nuoc",1);
-- 
-- create view FarmHasTypes as
-- select FarmID, Locate, Area, Farms.FarmTypeID, UserID, FarmType from Farms,FarmTypes
-- where Farms.FarmTypeID=FarmTypes.FarmTypeID;
-- -- drop view FarmHasTypes;
-- -- select * from FarmHasTypes;
-- create view DeviceHasTypes as
-- select DeviceID,Devices.DeviceTypeID,DeviceName,DeviceFunction,FarmID,DeviceType from Devices, DeviceTypes
-- where Devices.DeviceTypeID=DeviceTypes.DeviceTypeID;
-- -- drop view DeviceHasTypes;
-- -- select * from DeviceHasTypes;
-- 
-- create view Controller as
-- select Devices.DeviceID, DeviceName, DeviceFunction, FarmID, EnviInfoID, AmountOfWater, WateringDuration, LastUpdate from Controlling, Devices
-- where Devices.DeviceID=Controlling.DeviceID;
-- -- drop view Controller;
-- -- select * from Controller;
-- 
-- create view Monitor as
-- select Devices.DeviceID, DeviceName, DeviceFunction, FarmID, EnviInfoID, LastUpdate from Devices, Monitoring
-- where Devices.DeviceID=Monitoring.DeviceID;
-- -- drop view Monitoring;
-- -- select * from Monitoring;
-- 
-- -- select Devices.DeviceID, DeviceName, DeviceFunction, FarmID, EnviInfoID, AmountOfWater, WateringDuration, LastUpdate from Controlling, Devices
-- 
-- select LastUpdate from Monitoring where LastUpdate = 
-- (select max(LastUpdate) from Devices);
-- 
-- select * from Controlling where DeviceID = 1 and LastUpdate =
-- (select max(LastUpdate) from Controlling);