CREATE TABLE calendars
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    week_day_of_first_of_year VARCHAR(20) NOT NULL,
    is_leap BOOLEAN NOT NULL,
    CONSTRAINT uq_calendars_key UNIQUE (week_day_of_first_of_year, is_leap)
);

CREATE TABLE months
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    calendar_id BIGINT NOT NULL,
    month_number INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT fk_months_calendar FOREIGN KEY (calendar_id) REFERENCES calendars (id) ON DELETE CASCADE,
    CONSTRAINT uq_months_calendar_number UNIQUE (calendar_id, month_number)
);

CREATE TABLE days
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    month_id BIGINT NOT NULL,
    day_of_month INT NOT NULL,
    week_day VARCHAR(20) NOT NULL,
    CONSTRAINT fk_days_month FOREIGN KEY (month_id) REFERENCES months (id) ON DELETE CASCADE,
    CONSTRAINT uq_days_month_day UNIQUE (month_id, day_of_month)
);

CREATE INDEX idx_months_calendar_id ON months (calendar_id);
CREATE INDEX idx_days_month_id ON days (month_id);