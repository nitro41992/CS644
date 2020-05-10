## Big Data Analytics

- Rule-based vs Machine Learning approach
    - Rule
        - Explicitly programmed
        - Clearly defined
    - ML
        - Implicitly Trained
        - Complex and fuzzy

- Data Analytics Process
    - Data Sourcing
    - Data Cleaning
    - Data Combining
    - Data Reduction
    - Machine Learning
    - Knowledge Presentation

- Main steps for a Machine Learning solution
     - Define target
     - Gather data
     - Feature engineering
     - Model fit
     - Model evaluation

- 5 Questions and what algorithm can be used
    - Is it A or B?
        - Classification
    - Is this weird?
        - Anomaly Detection
    - How much or how many?
        - Regression
    - How is this organized?
        - Clustering
    - What should I do now?
        - Reinforcement Learning

- When to use Machine Learning? (Cheat Sheet)
    1) Do you need automation? 
    2) Is it too complex to be rule-based? 
    3) Can you formulate your problem clearly?
    4) Do you have data?
    5) Does your problem have a regular pattern?
    6) Can you find meaningful representations of your data?
    7) Can you measure success?

- Mahout - API functions that implement ML and AI algorithms
    - Collaborative Filtering
        - Item and User based
    - Classification
        - Naive Bayes
        - Complementary Naive Bayes
        - Random Forest
        - Stochastic Gradient Descent (SDG)
        - Support Vector Machine (SVM)
    - Clustering
        - Canopy Clustering
        - k-Means Clustering
        - Fuzzy k-Means

- User-based and Item-based algorithms
    - User-based
        - Gather a vector of User x's ratings
        - Find neighborhood of users most similar to User x who have also rated Item I
        - Predict User x's affinity for Item I using a weighted average of similarity ratings of the other users
    - Item-based
        - For Item I, find other similar items
        - Find neighborhood of items similar to Item I
        - Predict Item I's affinity for User x using a weighted average of similarity ratings of the other items
    - User Pearson Correlation for similarity ratings

- 3 main issues of Pearson Correlation
    - Does not take into account items of overlap between two users
    - Correlation cannot be computed for only 1 overlap item between two users
    - Undefined if preference values are identical

- MapReduce with k-means
    - Each Mapper reads in the centroids at startup
    - Each Mapper computes the relative centroids for a vector
    - Each Reducer gets the partial sums from Mappers and recomputes the centroids
    - Repeated until converged

- Distance Measurements
    - Pearson
    - Cosine
    - Manhattan
    - Euclidean
- Supervised vs. Unsupervised Learning
- Naive Bayes Technical Details
- Cost Functions
    - False Alarm
    - Missed Detection

## Big Data Visualization

- Rasterization: converting a vector image (shapes) to a raster image (dots)

- Two ways to represent images
    - Vector
        - Instruction based (not pixel-grid based)
        - Resolution independent
        - Requires very little memory
        - Must be rasterized
        - Used for text, diagrams and mechanical drawings
    - Raster
        - Distribution of intensity or color defined on a 2D plane
        - All displays are raster displays
        - To increase resolution, increase number of pixels
        - Memory intensive
        - Aliasing issues when image sampling

- Rendering Process
    - Compute color of original geometry based on light and color (Shading)
    - Project original 3D geometry to 2D model (Projection)
        - How images are drawn
    - Clip original geometry outside of FOV (Clipping)
    - Generate fragments from projected 2D model (Rasterization)
    - Compute pixel colors from fragments (Fragment Processing)

- Data Type of Raster images
    - Bitmaps
        - Boolean per pixel
    - Grayscale
        - Integer per pixel
        - Usually byte (8 bpp)
    - Color
        - 3 integers per pixel
        - Usually byte (24 bpp)
    - Floating point
        - Abstract representation due to infinite range
        - High dynamic range
        - Becoming standard in GPU
    - Clipping
        - Converted from floating point into an integer
        - Maximum value of display's output range becomes full intensity
        
- Transfer function
    - Pixel values represent intensity or brightness
        - Bigger numbers equal brighter pixel values
    - Function that maps the input pixel to luminance of the displayed image
        - Determined by the display's physical constraints

- Dynamic Range
    - Determines display achievable image contrast
    - I_max / I_min or (I_max + k / I_min + k)
        - I_max is maximum displayable intensity
        - I_min is minimum displayable intensity
        - k is reflected light by display
            - Usually I_max * 0.05

- Levels needed for given dynamic range
```    
    R_d = I_max / I_min 
    =>  I_max / I_min = (1.02)^N 
    => N = log(R_d) / log(1.02)
```

- Pixel value range needed given dynamic range
    - Pixel Range = R_d / 0.02

- Converting Pixel Resolution (Quantization)
    - Banding can occur with changes in intensity > 2%
    - Darker tones have lower intensity values
        - Intensity changes are more significant due to larger percent changes
            - 1 to 2 = 100% change vs 198 - 200 = 1% change
    - Logarithmic is the most efficient

- Ray Tracing algorithm
```
for each pixel in the 2D viewing plane
    {
        1) Compute a viewing ray
        2) Intersect ray with scene and find a visible point
        3) Compute illumination at the visible point
        4) Put result into image
    }
```

- Ray Casting
    - Surface approximation used by sampling results of a voxel grid via a ray
        - Sampling distance is user-defined and should be less than the size of a voxel
    - User-defined transfer functions are used ton convert samples to color and alpha values

- Marching Cubes
    - Creation of a mesh from an implicit function (one of the form f(x, y, z) = 0). 
        - Iterates  over a uniform grid of cubes superimposed over a region of the function.

- Techniques used for Big Data Visualization
    - Pixel Oriented
        - Using pixel intensity to display a column of data values based on the data range
    - Aggregation and Level of Details (LOD)
        - A tree for aggregating data items in either a bottom-up or top-down approach
        - Allows for a roll-up and drill-down of aggregation
    - Distortion
        - Allowing manipulations of regions of dat ain order to provide granular details
            - Magnifying
        - Scaling
    - Clutter Reduction
        - Reduces data noise
            - Sampling
            - Re-ordering 
            - Edge bundling 
    - Query-based
        - Search
        - Join
        - Merge
        - Group
        - Order
        - etc.

## High-Performance Networks

- Motivation for high performance networks
    - Internet is not sufficient for meeting Big Data applications
        - Only backbone has high bandwidths (last mile)
        - Packet-level resource sharing
        - Best-effort IP routing
        - TCP: hard to sustain 10s Gbps or to stabilize

- TCP/IP network stack
    - 4 layers
        - Application
            - Formats data
        - Transport
            - Packetize the data
        - Network
            - Add source and destination IP to each packet
        - Link
            - Adds source and destination MAC address to each packet
            - Passes to NIC drivers
    - Process
        1) Sender broadcasts an ARP request to router
        2) If the intended recipient is on a different sub-network, Router responds with its own MAC address
            - If the intended recipient is on the same sub-network, the MAC address of that recipient is sent to the sender that made the ARP request.
            - The MAC address is added as the destination of the packet
            - The packet is sent to the recipient
        3) Sender replaces MAC address of the destination on each packet
        4) Router sends an ARP request to all connected networks, including other routers
        5) The process is repeated until a router can identify the recipient based on the destination IP address
        6) If the recipient is found, the MAC address of that recipient is sent to the last router or sender that made the ARP request.
            - A chain is thus created where each router in between the sender and receiver contains the MAC address only of the source of the ARP response.
            - The MAC address is added as the destination of the packet
            - The packet is sent to the recipient
        

