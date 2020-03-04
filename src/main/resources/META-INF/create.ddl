CREATE TABLE mneme_user ("id" SERIAL PRIMARY KEY, "login" VARCHAR(100) NOT NULL UNIQUE, "email" VARCHAR(255) UNIQUE, "password" VARCHAR(255) NOT NULL, "mneme_group" VARCHAR(50) NOT NULL);
CREATE TABLE host_provider ("id" SERIAL PRIMARY KEY, "title" VARCHAR(50) NOT NULL UNIQUE);
CREATE TABLE track ("id" SERIAL PRIMARY KEY, "title" VARCHAR(255) NOT NULL, "host_provider_id" INTEGER REFERENCES host_provider, "track_id_in_host_provider" VARCHAR(255) NOT NULL, "description" TEXT, "thumbnail" VARCHAR(255), "availability" BOOLEAN NOT NULL, UNIQUE("host_provider_id", "track_id_in_host_provider"));
CREATE TABLE playlist ("id" SERIAL PRIMARY KEY, "title" VARCHAR(255) NOT NULL,  "host_provider_id" INTEGER REFERENCES host_provider, "playlist_id_in_host_provider" VARCHAR(255) NOT NULL, "owner_id" VARCHAR(255) NOT NULL, "availability" BOOLEAN NOT NULL, UNIQUE("host_provider_id", "playlist_id_in_host_provider"));
CREATE TABLE mneme_user_playlist ("mneme_user_id" INTEGER REFERENCES mneme_user, "playlist_id" INTEGER REFERENCES playlist, UNIQUE("mneme_user_id", "playlist_id"));
CREATE TABLE mneme_user_host_provider ("mneme_user_id" INTEGER REFERENCES mneme_user, "host_provider_id" INTEGER REFERENCES host_provider, "user_id_in_host_provider" VARCHAR(255), "refresh_token" VARCHAR(255) NOT NULL, PRIMARY KEY ("mneme_user_id", "host_provider_id"));
CREATE TABLE playlist_track ("playlist_id" INTEGER REFERENCES playlist, "track_id" INTEGER REFERENCES track, PRIMARY KEY ("playlist_id", "track_id"));