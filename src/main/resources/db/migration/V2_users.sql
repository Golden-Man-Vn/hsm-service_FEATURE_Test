-- public.users definition
-- Drop table
-- DROP TABLE public.users;
CREATE TABLE public.users (
	id serial4 NOT NULL,
	user_name varchar(100) NOT NULL,
	full_name varchar(100) NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	updated_at timestamp NOT NULL DEFAULT now(),
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_user_name_unique UNIQUE (user_name)
);
CREATE INDEX users_user_name_idx ON public.users USING btree (user_name);

-- Table Triggers
create trigger trigger_users_updated_at before
update
    on
    public.users for each row execute function set_updated_at();