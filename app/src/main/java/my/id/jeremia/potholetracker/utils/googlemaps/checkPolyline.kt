package my.id.jeremia.potholetracker.utils.googlemaps

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.operation.distance.DistanceOp

fun isPointOnPolyline(pointCoord: Coordinate, lineString: LineString): Boolean {
    val geometryFactory = GeometryFactory()
    val point = geometryFactory.createPoint(pointCoord)
    val distanceOp = DistanceOp(point, lineString)
    val distance = distanceOp.distance()
    val tolerance = 1e-6
    return distance < tolerance
}