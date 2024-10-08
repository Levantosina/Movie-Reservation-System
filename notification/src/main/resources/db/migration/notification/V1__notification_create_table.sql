CREATE TABLE notification (
                              notification_id SERIAL PRIMARY KEY,
                              to_user_id SERIAL,
                              sender VARCHAR ,
                              message VARCHAR ,
                              sent_at TIMESTAMP
);
ALTER TABLE public.notification ALTER COLUMN to_user_id DROP NOT NULL;
