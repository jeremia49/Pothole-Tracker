package my.id.jeremia.potholetracker.utils.googlemaps

typealias Point = DoubleArray

fun Point.sqd(p: Point) = this.zip(p) { a, b -> (a - b) * (a - b) }.sum()

class HyperRect (val min: Point, val max: Point) {
    fun copy() = HyperRect(min.copyOf(), max.copyOf())
}

data class NearestNeighbor(val nearest: Point?, val distSqd: Double, val nodesVisited: Int)

class KDNode(
    val domElt: Point,
    val split: Int,
    var left:  KDNode?,
    var right: KDNode?
)

class KDTree(pts: MutableList<Point>, val bounds: HyperRect) {
    val n: KDNode?

    init {
        fun nk2(exset: MutableList<Point>, split: Int): KDNode? {
            if (exset.size == 0) return null
            val exset2 = exset.sortedBy { it[split] }
            for (i in 0 until exset.size) exset[i] = exset2[i]
            var m = exset.size / 2
            val d = exset[m]
            while (m + 1 < exset.size && exset[m + 1][split] == d[split]) m++
            var s2 = split + 1
            if (s2 == d.size) s2 = 0
            return KDNode(
                d,
                split,
                nk2(exset.subList(0, m), s2),
                nk2(exset.subList(m + 1, exset.size), s2)
            )
        }
        this.n = nk2(pts, 0)
    }

    fun nearest(p: Point) = nn(n, p, bounds, Double.POSITIVE_INFINITY)

    private fun nn(
        kd: KDNode?,
        target: Point,
        hr: HyperRect,
        maxDistSqd: Double
    ): NearestNeighbor {
        if (kd == null) return NearestNeighbor(null, Double.POSITIVE_INFINITY, 0)
        var nodesVisited = 1
        val s = kd.split
        val pivot = kd.domElt
        val leftHr = hr.copy()
        val rightHr = hr.copy()
        leftHr.max[s] = pivot[s]
        rightHr.min[s] = pivot[s]
        val targetInLeft = target[s] <= pivot[s]
        val nearerKd = if (targetInLeft) kd.left else kd.right
        val nearerHr = if (targetInLeft) leftHr else rightHr
        val furtherKd = if (targetInLeft) kd.right else kd.left
        val furtherHr = if (targetInLeft) rightHr else leftHr
        var (nearest, distSqd, nv) = nn(nearerKd, target, nearerHr, maxDistSqd)
        nodesVisited += nv
        var maxDistSqd2 = if (distSqd < maxDistSqd) distSqd else maxDistSqd
        var d = pivot[s] - target[s]
        d *= d
        if (d > maxDistSqd2) return NearestNeighbor(nearest, distSqd, nodesVisited)
        d = pivot.sqd(target)
        if (d < distSqd) {
            nearest = pivot
            distSqd = d
            maxDistSqd2 = distSqd
        }
        val temp = nn(furtherKd, target, furtherHr, maxDistSqd2)
        nodesVisited += temp.nodesVisited
        if (temp.distSqd < distSqd) {
            nearest = temp.nearest
            distSqd = temp.distSqd
        }
        return NearestNeighbor(nearest, distSqd, nodesVisited)
    }
}
