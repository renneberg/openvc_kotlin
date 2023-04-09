package library.classes

import org.opencv.core.Mat
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.ORB
import org.opencv.features2d.SIFT
import org.opencv.xfeatures2d.SURF

class DetectAndComputeFeatures {
}

fun detectAndComputeFeatures_SIFT(img: Mat): Pair<MatOfKeyPoint, Mat> {
    val detector = SIFT.create()
    val keypoints = MatOfKeyPoint()
    val descriptors = Mat()
    detector.detectAndCompute(img, Mat(), keypoints, descriptors)
    return Pair(keypoints, descriptors)
}




fun detectAndComputeFeatures_SURF(img: Mat): Pair<MatOfKeyPoint, Mat> {
    val detector = SURF.create(800.0)
    val keypoints =MatOfKeyPoint()
    val descriptors = Mat()
    detector.detectAndCompute(img, Mat(), keypoints, descriptors)
    return Pair(keypoints, descriptors)
}


fun detectAndComputeFeture_Orb(img: Mat): Pair<MatOfKeyPoint, Mat> {
    // Create ORB object
    val orb = ORB.create()
    // Detect and compute features
    val keypoints = MatOfKeyPoint()
    val descriptors = Mat()
    orb.detectAndCompute(img, Mat(), keypoints, descriptors)
    return Pair(keypoints, descriptors)
}