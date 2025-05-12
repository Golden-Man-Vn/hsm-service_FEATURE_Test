-- public.tasks_detail definition
-- Drop table
-- DROP TABLE public.tasks_detail;
CREATE TABLE public.tasks_detail (
	id serial4 NOT NULL,
	"name" varchar(255) NOT NULL DEFAULT ''::character varying,
	"type" int4 NOT NULL DEFAULT 0,
	created_at timestamp NOT NULL DEFAULT now(),
	updated_at timestamp NOT NULL DEFAULT now(),
	note varchar NULL,
	bug_severity int4 NULL,
	bug_steps_to_reproduce varchar NULL,
	feature_business_value int4 NULL,
	feature_deadline timestamp NULL,
	CONSTRAINT tasks_detail_pk PRIMARY KEY (id)
);
CREATE INDEX tasks_detail_bug_severity_idx ON public.tasks_detail USING btree (bug_severity);
CREATE INDEX tasks_detail_created_at_idx ON public.tasks_detail USING btree (created_at);
CREATE INDEX tasks_detail_type_idx ON public.tasks_detail USING btree (type);

-- Table Triggers
create trigger trigger_tasks_detail_updated_at before
update
    on
    public.tasks_detail for each row execute function set_updated_at();