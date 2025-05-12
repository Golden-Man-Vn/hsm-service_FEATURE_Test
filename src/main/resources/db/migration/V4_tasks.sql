-- public.tasks definition
-- Drop table
-- DROP TABLE public.tasks;
CREATE TABLE public.tasks (
	id serial4 NOT NULL,
	users_id int4 NOT NULL,
	task_detail_id int4 NOT NULL,
	status int4 NOT NULL DEFAULT 0,
	note varchar NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	updated_at timestamp NOT NULL DEFAULT now(),
	CONSTRAINT tasks_pk PRIMARY KEY (id)
);
CREATE INDEX tasks_created_at_idx ON public.tasks USING btree (created_at);
CREATE INDEX tasks_status_idx ON public.tasks USING btree (status);


-- public.tasks foreign keys
ALTER TABLE public.tasks ADD CONSTRAINT tasks_fk1 FOREIGN KEY (users_id) REFERENCES public.users(id);
ALTER TABLE public.tasks ADD CONSTRAINT tasks_fk2 FOREIGN KEY (task_detail_id) REFERENCES public.tasks_detail(id);