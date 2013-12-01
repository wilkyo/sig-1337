select pgr_createTopology('osm_2po_4pgr',0.000001,'geom_way');
select pgr_analyzeGraph('osm_2po_4pgr',0.000001,'geom_way');
select pgr_nodeNetwork('osm_2po_4pgr',0.000001,'id', 'geom_way');
select pgr_createTopology('osm_2po_4pgr_noded',0.000001,'geom_way');
select pgr_analyzeGraph('osm_2po_4pgr_noded',0.000001,'geom_way');
create view get_source as 
	(SELECT
		ST_X(vertices.the_geom) as point_x,
		ST_Y(vertices.the_geom) as point_y,
		ST_X(req.voisin) as voisin_x,
		ST_Y(req.voisin) as voisin_y
	FROM 
		( SELECT
			req.req_id as id,
			vertices.the_geom as voisin
		FROM 
			( SELECT
				vertices.id AS req_id,
				edges.source AS voisin
			FROM
				osm_2po_4pgr_noded edges,
				osm_2po_4pgr_noded_vertices_pgr vertices
			WHERE 
				edges.target = vertices.id) req,
			osm_2po_4pgr_noded_vertices_pgr vertices
		WHERE
			vertices.id = req.voisin) as req,
		public.osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		req.id = vertices.id 
	ORDER BY
		req.id);
create view get_target as 
	(SELECT
		ST_X(vertices.the_geom) as point_x,
		ST_Y(vertices.the_geom) as point_y,
		ST_X(req.voisin) as voisin_x,
		ST_Y(req.voisin) as voisin_y
	FROM 
		( SELECT
			req.req_id as id,
			vertices.the_geom as voisin
		FROM 
			( SELECT
				vertices.id AS req_id,
				edges.target AS voisin
			FROM
				osm_2po_4pgr_noded edges,
				osm_2po_4pgr_noded_vertices_pgr vertices
			WHERE 
				edges.source = vertices.id) req,
			osm_2po_4pgr_noded_vertices_pgr vertices
		WHERE
			vertices.id = req.voisin) as req,
		public.osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		req.id = vertices.id 
	ORDER BY
		req.id);