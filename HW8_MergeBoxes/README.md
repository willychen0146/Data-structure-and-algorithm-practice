# HW8: Merge Boxes (Interval Search Tree and Sweep Line Algorithm)

Left: Before merge; Right: After merge

![](https://hackmd-prod-images.s3-ap-northeast-1.amazonaws.com/uploads/upload_69bbefcaaffe2a4913c21afb773ad3d7.png?AWSAccessKeyId=AKIA3XSAAW6AWSKNINWO&Expires=1715506263&Signature=mipU8TxEJ9Mi06nz2tPe%2BdbP8pw%3D)

## Concept Explanation:
### Bounding Boxes  
In computer vision, we often want to identify objects using algorithms. To visualize the results of object detection, we will draw boxes or circles around the object on the original image/video. For a better understanding, see the red boxes in the left part of the picture at the top of this document.

A bounding box can be described using the (x, y, w, h) format, where (x, y) represents the coordinates of the up-left corner of the box, and (w, h) denotes its width and height. These coordinates follow the convention where the origin (0, 0) is situated at the up-left corner of the whole image. Note that the (x, y, w, h) format will be the input format for bounding boxes in this problem. You might need to do some calculations to get the coordinates of the other points.

**Here's an example of a bounding box:**

- The origin (0, 0) is at the up-left corner.
- The box's up-left corner is at (0,0), denoted as (leftX, upY)
- The box's down-right corner is at (1,1), denoted as (rightX, downY)
- Based on the information above, the width and height are both 1.
- Using (x,y,w,h) format, it will be described as (0,0,1,1)

![](https://hackmd-prod-images.s3-ap-northeast-1.amazonaws.com/uploads/upload_38e3c600292cc5ba721ea6319a2999a2.png?AWSAccessKeyId=AKIA3XSAAW6AWSKNINWO&Expires=1715506445&Signature=jRu3kOpSx9h8DxoMjkZAgrPWrl8%3D)

### Merging boxes  

In this problem, we define the "merging" process of two boxes (box1 and box2) as follows:

- The leftX coordinate of merged box will be min(box1.leftX, box2.leftX)
- The upY coordinate of merged box will be min(box1.upY, box2.upY)
- The rightX coordinate of merged box will be max(box1.rightX, box2.rightX)
- The downY coordinate of merged box will be max(box1.downY, box2.downY)

Here's a visual explanation of the merging process:

![](https://hackmd-prod-images.s3-ap-northeast-1.amazonaws.com/uploads/upload_d7c3fa612386a47c209be8310de48303.jpeg?AWSAccessKeyId=AKIA3XSAAW6AWSKNINWO&Expires=1715506472&Signature=WIv1ximcGUxmVxAnfwUJL1xaZxQ%3D)

### IoU value (intersection of union)  

The IoU value is a way of calculating how much two bounding boxes overlap.

![](https://hackmd-prod-images.s3-ap-northeast-1.amazonaws.com/uploads/upload_ffbd784478d952c3d30d92d73b94dbb8.png?AWSAccessKeyId=AKIA3XSAAW6AWSKNINWO&Expires=1715506498&Signature=DmaVHk31qPWSykixojH11ZpC%2BxU%3D)

*Area of Union = Area1 + Area2 - Area of Overlap.*

## Problem  

In this problem, you are given N bounding boxes. Check all of them and merge the boxes if:

\[IoU(box_i,box_j) >= IoUthreshold\]  
\[0 <= i < N\]  
\[0 <= j < N\]  
\[i != j\]  

After you merge all the pairs that need to be merged, sort and return the merged boxes by the following priorities:

1. leftX by ascending order
2. upY by ascending order
3. width by ascending order
4. height by ascending order

### Hint  

Interval Search Tree  

## Template  

```java
class ImageMerge {
    public double[][] mergeBox()
    {
        //return merged bounding boxes just as input in the format of 
        //[up_left_x,up_left_y,width,height]
    }
    public ImageMerge(double[][] bbs, double iou_thresh){
        //bbs(bounding boxes): [up_left_x,up_left_y,width,height]
        //iou_threshold:          [0.0,1.0]
    }
    public static void draw(double[][] bbs)
    {
        // ** NO NEED TO MODIFY THIS FUNCTION, WE WON'T CALL THIS **
        // ** DEBUG ONLY, USE THIS FUNCTION TO DRAW THE BOX OUT** 
        StdDraw.setCanvasSize(960,540);
        for(double[] box : bbs)
        {
            double half_width = (box[2]/2.0);
            double half_height = (box[3]/2.0);
            double center_x = box[0]+ half_width;
            double center_y = box[1] + half_height;
            //StdDraw use y = 0 at the bottom, 1-center_y to flip
            StdDraw.rectangle(center_x, 1-center_y, half_width,half_height);
        }
    }
    public static void main(String[] args) {
        ImageMerge sol = new ImageMerge(
                new double[][]{
                        {0.02,0.01,0.1,0.05},{0.0,0.0,0.1,0.05},{0.04,0.02,0.1,0.05},{0.06,0.03,0.1,0.05},{0.08,0.04,0.1,0.05},
                        {0.24,0.01,0.1,0.05},{0.20,0.0,0.1,0.05},{0.28,0.02,0.1,0.05},{0.32,0.03,0.1,0.05},{0.36,0.04,0.1,0.05},
                },
                0.5
        );
        double[][] temp = sol.mergeBox();
        ImageMerge.draw(temp);
    } 
}
```

## Test Data
```java
Time Limit: 1234ms

N = Number of Boxes
0.0 <= IoU <= 1.0

Case:
case1: 20 points, N <= 200
case2: 20 points, N <= 1000
case3: 20 points, N <= 4000
case4: 20 points, N <= 8000
case5: 20 points, N <= 10000
```

## File Download
[Test Code](https://drive.google.com/file/d/1jKnbXlejbGI4jf6PGkymAvZ0C7iZiFzt/view)

[Test Data](https://drive.google.com/file/d/1iZE9IjuPTHIlukIqYgLn5nnKmZ6SRTf5/view)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/@CiqLOooyRwWmK--mMkfetA/r1-Z_mjsT)