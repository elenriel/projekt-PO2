CREATE DATABASE TicketReservation;
GO
USE TicketReservation;
GO

CREATE TABLE Clients (
    ClientId INT IDENTITY(1,1) PRIMARY KEY,
    FirstName NVARCHAR(50) NOT NULL,
    LastName  NVARCHAR(50) NOT NULL,
    Email     NVARCHAR(100) NULL
);

CREATE TABLE Events (
    EventId INT IDENTITY(1,1) PRIMARY KEY,
    Title NVARCHAR(100) NOT NULL,
    Location NVARCHAR(100) NOT NULL
);

CREATE TABLE Shows (
    ShowId INT IDENTITY(1,1) PRIMARY KEY,
    EventId INT NOT NULL FOREIGN KEY REFERENCES Events(EventId),
    StartTime DATETIME2 NOT NULL,
    Price DECIMAL(10,2) NOT NULL
);

CREATE TABLE Reservations (
    ReservationId INT IDENTITY(1,1) PRIMARY KEY,
    ClientId INT NOT NULL FOREIGN KEY REFERENCES Clients(ClientId),
    ShowId   INT NOT NULL FOREIGN KEY REFERENCES Shows(ShowId),
    SeatNo   INT NOT NULL,
    CreatedAt DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
    Status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT UQ_Show_Seat UNIQUE(ShowId, SeatNo)
);
GO

INSERT INTO Clients(FirstName, LastName, Email) VALUES
('Anna','Nowak','anna@x.pl'),
('Jan','Kowalski','jan@x.pl');

INSERT INTO Events(Title, Location) VALUES
('Koncert - Rock Night','Gdańsk'),
('Teatr - Komedia','Warszawa');

INSERT INTO Shows(EventId, StartTime, Price) VALUES
(1, '2026-02-01 19:00', 120.00),
(1, '2026-02-02 19:00', 120.00),
(2, '2026-02-05 18:00', 80.00);
GO

CREATE OR ALTER FUNCTION dbo.fn_ShowPrice(@ShowId INT)
RETURNS DECIMAL(10,2)
AS
BEGIN
    DECLARE @p DECIMAL(10,2);
    SELECT @p = Price FROM Shows WHERE ShowId = @ShowId;
    RETURN ISNULL(@p, 0);
END
GO

CREATE OR ALTER VIEW dbo.v_ReservationList
AS
SELECT r.ReservationId, r.Status, r.SeatNo, r.CreatedAt,
       c.FirstName, c.LastName, c.Email,
       e.Title, e.Location,
       s.StartTime, s.Price
FROM Reservations r
JOIN Clients c ON c.ClientId = r.ClientId
JOIN Shows s ON s.ShowId = r.ShowId
JOIN Events e ON e.EventId = s.EventId;
GO

CREATE OR ALTER PROCEDURE dbo.sp_CreateReservation
    @ClientId INT,
    @ShowId INT,
    @SeatNo INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        IF EXISTS (SELECT 1 FROM Reservations WHERE ShowId=@ShowId AND SeatNo=@SeatNo AND Status='ACTIVE')
        BEGIN
            RAISERROR('Seat already reserved', 16, 1);
        END

        INSERT INTO Reservations(ClientId, ShowId, SeatNo)
        VALUES (@ClientId, @ShowId, @SeatNo);

        COMMIT;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK;
        THROW;
    END CATCH
END
GO
