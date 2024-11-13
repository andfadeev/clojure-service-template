CREATE TABLE weather_forecast
(
    time TEXT,
    temperature TEXT,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
