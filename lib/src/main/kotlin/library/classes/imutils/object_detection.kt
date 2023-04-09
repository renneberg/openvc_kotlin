package library.classes.imutils

import org.opencv.core.Rect

fun nonMaxSuppression(boxes: Array<FloatArray>, probs: FloatArray? = null, overlapThresh: Float = 0.3f): List<Int> {
    // if there are no boxes, return an empty list
    if (boxes.isEmpty()) {
        return emptyList()
    }

    // if the bounding boxes are integers, convert them to floats -- this
    // is important since we'll be doing a bunch of divisions
    if (boxes[0].all { it.isNaN().not() && it.toInt().toFloat() == it }) {
        for (i in boxes.indices) {
            for (j in 0 until boxes[i].size) {
                boxes[i][j] = boxes[i][j].toInt().toFloat()
            }
        }
    }

    // initialize the list of picked indexes
    val pick = mutableListOf<Int>()

    // grab the coordinates of the bounding boxes
    val x1 = boxes.map { it[0] }
    val y1 = boxes.map { it[1] }
    val x2 = boxes.map { it[2] }
    val y2 = boxes.map { it[3] }

    // compute the area of the bounding boxes and grab the indexes to sort
    // (in the case that no probabilities are provided, simply sort on the
    // bottom-left y-coordinate)
    val area = boxes.map { (it[2] - it[0] + 1) * (it[3] - it[1] + 1) }
    val idxs:MutableList<Int> = ((probs?.mapIndexed { index, _ -> index } ?: y2.indices.toList()) as MutableList<Int>)

    // if probabilities are provided, sort on them instead
    if (probs != null) {
        idxs.sortedByDescending { probs[it] }
    } else {
        idxs.sortedBy { y2[it] }
    }

    // keep looping while some indexes still remain in the indexes list
    while (idxs.isNotEmpty()) {
        // grab the last index in the indexes list and add the index value
        // to the list of picked indexes
        val last = idxs.last()
        pick.add(last)

        // find the largest (x, y) coordinates for the start of the bounding
        // box and the smallest (x, y) coordinates for the end of the bounding
        // box
        val xx1 = idxs.dropLast(1).map { x1[it] }.map { it.coerceAtLeast(x1[last]) }
        val yy1 = idxs.dropLast(1).map { y1[it] }.map { it.coerceAtLeast(y1[last]) }
        val xx2 = idxs.dropLast(1).map { x2[it] }.map { it.coerceAtMost(x2[last]) }
        val yy2 = idxs.dropLast(1).map { y2[it] }.map { it.coerceAtMost(y2[last]) }

        // compute the width and height of the bounding box
        val w = xx2.zip(xx1).map { (a, b) -> (a - b + 1).coerceAtLeast(0f) }
        val h = yy2.zip(yy1).map { (a, b) -> (a - b + 1).coerceAtLeast(0f) }

        // compute the ratio of overlap
        val overlap = idxs.dropLast(1).mapIndexed { index, _ ->
            (w[index] * h[index]) / area[index]
        }

        idxs.removeAll { it != last && overlap[idxs.indexOf(it) - 1] > overlapThresh }
    }

    // Return only the bounding boxes that were picked
    return pick
}

