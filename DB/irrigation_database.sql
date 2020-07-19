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
	LocateID varchar(50) not null,
    LocateName varchar(50),
    primary key(LocateID)
) engine InnoDB default charset=utf8;
create table if not exists Farms(
	FarmID int not null auto_increment,
    LocateID varchar(50),
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
    -- SoilTemperature float4,
    LightLevel int,
    TimeOfMeasurement datetime,
    primary key(SensingID),
    foreign key(PlotID) references Plots(PlotID) on delete restrict on update cascade,
    foreign key(DeviceID) references Devices(DeviceID) on delete restrict on update cascade
) engine InnoDB default charset=utf8;

create table if not exists WeatherForecasts(
	WeatherForecastID int not null auto_increment,
    LocateID varchar(50) not null,
    CurrentTime timestamp,
    primary key(WeatherForecastID),
    foreign key(LocateID) references Locates(LocateID) on delete restrict on update cascade
) engine InnoDB default charset=utf8;

create table if not exists WeatherForecastAtATimes(
	WeatherForecastID int not null,
	ForecastTime timestamp,
    EpochTime bigint,
    ForecastStatus varchar(50),
    IsDayLight bool,
    Temperature float,
    WindSpeed float,
    RelativeHumidity float,
    RainProbability tinyint,
    PrecipitationProbability tinyint,
    RainValue float,
    CloudCover tinyint,
    primary key(WeatherForecastID, ForecastTime),
    foreign key(WeatherForecastID) references WeatherForecasts(WeatherForecastID) on delete restrict on update cascade
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

insert into Users(Users.UserName, Users.UPassword)
	select * from (select "admin",md5("12345678")) as tmp
    where not exists(
     select UserName from Users where UserName="admin"
    ) limit 1;

insert into Locates(LocateID, locateName)
	select * from (select "353412","Ha Noi") as tmp
    where not exists(
     select LocateID from Locates where LocateID="353412"
    ) limit 1;

insert into DeviceTypes (DeviceType)
	select * from (select "Agriculture Sensor") as tmp
    where not exists(
     select DeviceType from DeviceTypes where DeviceType="Agriculture Sensor"
    ) limit 1;

insert into DeviceTypes (DeviceType)
	select * from (select "Pump") as tmp
    where not exists(
     select DeviceType from DeviceTypes where DeviceType="Pump"
    ) limit 1;


insert into FarmTypes(FarmType)
	select * from (select "Default") as tmp
    where not exists(
     select FarmType from FarmTypes where FarmType="Default"
    ) limit 1;


insert into Farms(LocateID,Area,FarmTypeID,UserID)
	select * from (select "353412" as a,1 as b,1 as c,1 as d) as tmp
    where not exists(
     select Area from Farms where Area=1
    ) limit 1;

insert into PlotTypes(PlotTypes.PlotType)
	select * from (select "Default") as tmp
    where not exists(
     select PlotType from PlotTypes where PlotType="Default"
    ) limit 1;
    
insert into Plots(Plots.PlotID, Plots.FarmID, Plots.PlotTypeID, Plots.Area)
	select * from (select 1 as a,1 as b,1 as c,1 as d) as tmp
    where not exists(
     select Area from Plots where Area=1
    ) limit 1;

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