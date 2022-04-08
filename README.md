<p align="center">
    <br />
    <br />
    <img src="img/IAR_GitHub_Banner.png" width="100%" alt="ImagineAR Logo" />
    <br />
    <br />
    <a href="https://docs.imaginear.com/" alt="ImagineAR-SDK" title="ImagineAR-SDK"><img src="img/version.svg" /></a>
    <a href="https://docs.dev.imaginear.com/docs/v1.5/android#imaginear--android-sdk-integration-guide" alt="Supported Platforms: Android" title="Supported Platforms: Android"><img src="img/platform.svg" /></a>
</p>

# IAR-SDK-Samples-Android

ImagineAR is an Augmented Reality app development platform with tools to enable AR experiences for digital audiences.  This software development kit integrates seamlessly into native mobile applications providing everything needed to create and manage engaging AR experiences.  The ImagineAR SDK leverages ImagineAR Cloud to provide self-publishing management tools used to define and deliver AR experiences to end users in real time.

**Want to learn more about ImagineAR?** Visit the [ImagineAR website](https://imaginear.com/).

# ImagineAR Features
**Marker Types**
-
ImagineAR provides a variety of features to help bridge the gap between users and the core AR camera experiences:

**Location Markers**

 - This marker type contains additional location metadata that can be compared to GPS data from the device as a gateway to accessing location-dependent user experiences
 - The ensuing AR experience for location markers leverages surface-detection to place the digital asset in the AR space

**Image Markers**

 - Leveraging the camera, image recognition is used to limit access to Image Marker experiences
 - Upload contrast-rich images to the Cloud dashboard to enable them as image targets, using them as access keys for AR experiences
 - Image Marker AR experiences leverage the interpreted image target as the "surface" anchor point for the digital content

**On-Demand Markers**

 - On-Demand Markers require no specific action by the end user to access the AR experience
 - Activation is facilitated simply by an ID-based lookup, where the Cloud service will request a marker ID and return the necessary metadata back to render the AR experience
 - These marker types can be used as building blocks for more complex, customizable experiences not bound by ImagineAR's guidelines
 - On-Demand Markers leverage surface-detection to place digital assets in the AR space

**NFC Markers**

 - Leveraging the flexibility of On-Demand Markers, the SDK provides tools to enable NFC tags to directly activate Cloud-defined AR experiences
 - Tools are offered to write marker information to any NFC tag using a mobile device
 - Similarly, a feature is present to read and launch the surface-detection AR experience with the NFC metadata

**Shared Features**
-
Additional shared features can be layered into the user experiences defined above to provide a richer experience for AR users:

**Rewards**

 - Each marker experience can be elevated with additional digital content in the form of Rewards
 - Rewards are user-owned digital objects that are retained beyond the single AR experience
 - Rewards come in two types: image assets or general promo code strings
 - Alongside each Reward, a link with custom text can be provided to help guide and convert users to other digital experiences

**AR Hunts**

 - Multiple marker experiences, regardless of type, can be grouped together to form an AR Hunt
 - AR Hunts aim to provide a method of encouraging users to continue to engage with more AR experiences while visiting the app
 - Rewards are given to users based on their progression through an AR Hunt, either for completion or for partial progress

**Video Capture & Sharing**

 - ImagineAR provides the technology to allow for screen capture of the AR experiences without including overlaid UI elements
 - This creates an ideal social-sharing product at the end of the experience
 - Users can capture both images and video of their experience, the media is held locally and can be shared or uploaded to any storage location if desired

**Anonymous User Management**

 - ImagineAR provides an ID-based user management system that can be paired with any user context from a consuming mobile app
 - The user system tracks ownership of rewards and progress through AR hunts, allowing support for users who want to engage on multiple devices
 - The system includes a migration feature to move user data between IDs
 - ImagineAR's user management system only stores an ID to represent the user, therefore it does not accept or hold any personally identifying information(PII) for a given user

**Asset Types**
-
The ImagineAR SDK can render the following digital content types in the AR camera experiences:

**3D Models**

 - Providing the most immersive experience, the SDK supports complex 3D model rendering
 - Animation is supported for 3D models, looping the animation in the scene
 - Real-time shadows are rendered based on the model's geometry during surface-detection experiences

**Videos**

 - The ImagineAR SDK is capable of rendering video assets to a 2D plane in the 3D AR scene
 - Video assets support green and blue chromakeying, with an adjustable threshold, this can provide a strong immersive experience for real world objects without creating 3D models
 - Videos are required to be in MP4 format

**Images**

 - Similar to video assets, basic image assets can also be rendered to a 2D plane and drawn in the 3D AR scene
 - Image assets can be provided in PNG or JPG format

Assets can be manipulated dynamically using metadata defined in the ImagineAR Cloud dashboard, allowing control over scale, offset positioning and rotation.

# How to Get Started

- Download this repository extract and open the folder.
- Use Android Studio to build the samples. If you don't have already, install [Android Studio](https://developer.android.com/studio), following the instructions on the website.

# The Samples

The ImagineAR SDK provides three sample applications to demonstrate the various features of the platform.  `surface-ar-sample` and `target-ar-sample` can be integrated independently based on the needs of the application, but both rely on functionality from `core-sample`.

## surface-ar-sample

Demonstrates the basic usage of the features contained within the `iar-surface-android`:

- Environment surface detection
- Dynamic acquisition of defined content metadata from ImagineAR Cloud
- Rendering and AR projection of all supported asset types using the detected surface
- Location Markers
- On-Demand Markers
- NFC Markers
- Video Capture & Sharing for surface AR experiences

![Surface SDK Sample](/img/SurfaceSample.png)

## target-ar-sample

Demonstrates the basic usage of the features contained within the `iar-target-android`:

- Recognition of target images for Image Marker experiences
- Dynamic acquisition of defined content metadata from ImagineAR Cloud
- Anchoring and projection of all supported asset types to the recognized target image
- Video Capture & Sharing for target AR experiences


**To invoke target markers on the demonstration account, scan these images within the sample App:**

- [Download images](https://iarv2storage.blob.core.windows.net/sdk-versions/TargetImages.zip)

![Target SDK Sample](/img/TargetSample.png)

## core-sample

Demonstrates the basic API-based content interactions with ImagineAR Cloud for the following features:

- User Rewards
- AR Hunts
- Location Markers
- On-Demand Markers

![Core SDK Sample](/img/CoreSample.png)

## common

All samples contain the following feature modules to support their respective features:

- User Management tools - including external user creation, login and migration functionality
- Debug Tools - a curated set of developer tools to help with configuration and debugging

## Requirements
- You need an Android device and Android development environment with minimum API 26.
- Only portrait mode is supported.

# Ready to Integrate?
The ImagineAR SDK is a paid service with flexible pricing, access to the ImagineAR Cloud dashboard is limited to our customers.

Read-only sample content is provided for test integrations to help ensure ImagineAR is the right tool for your application. If you're ready to test the integration into your mobile app, head to our [integration documentation](https://docs.dev.imaginear.com/docs/v1.5/android/integration#integrate-the-sdk-into-your-application) for all the details.

Have questions about integration or are ready to discuss pricing? Don't hesitate to reach out to us at [info@imaginear.com](https://imaginear.com/contact-us).


# License

The contents of this repository are licensed under the [Apache License, version 2.0.](https://www.apache.org/licenses/LICENSE-2.0)
The use of Imagine AR SDK is governed by the [Terms of Service](https://imaginear.com/terms-conditions) for ImagineAR.
