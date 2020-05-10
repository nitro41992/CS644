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

- Clustering
    - K-Means Clustering algorithm
        - How to implement MapReduce
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
    - Convert pixel to intensity values

- Dynamic Range
    - I max - I min

- Converting Pixel Resolution

- Minimum precision that is needed for an image without bands

- Ray Tracing algorithm

- Ray Casting

- Marching Cubes

- Techniques used for Big Data Visualization

## High-Performance Networks

- Why the current internet is not sufficient for meeting Big Data applications
    - Motivation for high performance networks
- TCP/IP network stack