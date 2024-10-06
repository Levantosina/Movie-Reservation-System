create table notification(
                             notification_id SERIAL PRIMARY KEY ,
                             to_user_id SERIAL NOT NULL ,
                             sender TEXT NOT NULL ,
                             message TEXT NOT NULL ,
                             sent_at TIME not null
);