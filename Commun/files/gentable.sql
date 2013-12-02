DROP VIEW IF EXISTS get_source;
DROP VIEW IF EXISTS getsource_sreq;
DROP VIEW IF EXISTS getsource_ssreq;
DROP VIEW IF EXISTS get_target;
DROP VIEW IF EXISTS gettarget_sreq;
DROP VIEW IF EXISTS gettarget_ssreq;
DROP VIEW IF EXISTS get_target;
DROP TABLE IF EXISTS osm_2po_4pgr_noded_vertices_pgr;
DROP TABLE IF EXISTS osm_2po_4pgr_noded;
DROP TABLE IF EXISTS osm_2po_4pgr_vertices_pgr;

SELECT pgr_createTopology('osm_2po_4pgr',0.000001,'geom_way');
SELECT pgr_analyzeGraph('osm_2po_4pgr',0.000001,'geom_way');
SELECT pgr_nodeNetwork('osm_2po_4pgr',0.000001,'id', 'geom_way');
SELECT pgr_createTopology('osm_2po_4pgr_noded',0.000001,'geom_way');
SELECT pgr_analyzeGraph('osm_2po_4pgr_noded',0.000001,'geom_way');

CREATE VIEW getsource_ssreq AS
	(SELECT
		vertices.id AS req_id,
		edges.source AS voisin
	FROM
		osm_2po_4pgr_noded edges,
		osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		vertices.id = edges.target);
CREATE VIEW getsource_sreq AS
	SELECT
		req.req_id AS id,
		vertices.the_geom AS voisin
	FROM
		getsource_ssreq req,
		osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		vertices.id = req.voisin;
CREATE VIEW get_source AS 
	SELECT
		ST_X(vertices.the_geom) AS point_x,
		ST_Y(vertices.the_geom) AS point_y,
		ST_X(req.voisin) AS voisin_x,
		ST_Y(req.voisin) AS voisin_y
	FROM 
		getsource_sreq req,
		osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		req.id = vertices.id 
	ORDER BY
		req.id;

		
CREATE VIEW gettarget_ssreq AS
	SELECT
		vertices.id AS req_id,
		edges.target AS voisin
	FROM
		osm_2po_4pgr_noded edges,
		osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		vertices.id = edges.source;
CREATE VIEW gettarget_sreq AS
	SELECT
		req.req_id AS id,
		vertices.the_geom AS voisin
	FROM
		gettarget_ssreq req,
		osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		vertices.id = req.voisin;
CREATE VIEW get_target AS 
	SELECT
		ST_X(vertices.the_geom) AS point_x,
		ST_Y(vertices.the_geom) AS point_y,
		ST_X(req.voisin) AS voisin_x,
		ST_Y(req.voisin) AS voisin_y
	FROM 
		gettarget_sreq req,
		osm_2po_4pgr_noded_vertices_pgr vertices
	WHERE
		req.id = vertices.id 
	ORDER BY
		req.id;

