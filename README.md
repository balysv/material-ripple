Material Ripple Layout
===============

Ripple effect wrapper for Android Views

![Demo Image][1]

Including in your project
-------------------------

```groovy
compile 'com.balysv:material-ripple:1.0.2'
```

Check for latest version number on the widget below or visit [Releases](https://github.com/balysv/material-ripple/releases)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.balysv/material-ripple/badge.svg?style=flat)](http://mvnrepository.com/artifact/com.balysv/material-ripple)

Usage
-----

Use static initializer on your `View` (see `xml` attributes below for customization)

```java
MaterialRippleLayout.on(view)
           .rippleColor(Color.BLACK)
           .create();
```

Or wrap your `View` with `MaterialRippleLayout` in your layout file:

```xml
<com.balysv.materialripple.MaterialRippleLayout
    android:id="@+id/ripple"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="Button inside a ripple"/>

</com.balysv.materialripple.MaterialRippleLayout>
```

If using in an `AdapterView` you must set `rippleInAdapter` to `true`


Configure using xml attributes or setters in code:

```java
app:mrl_rippleOverlay="true"              // if true, ripple is drawn in foreground; false - background
app:mrl_rippleColor="#ff0000"             // color of ripple
app:mrl_rippleAlpha="0.1"                 // alpha of ripple
app:mrl_rippleDimension="10dp"            // radius of hover and starting ripple
app:mrl_rippleHover="true"                // if true, a hover effect is drawn when view is touched
app:mrl_rippleRoundedCorners="10dp"       // radius of corners of ripples. Note: it uses software rendering pipeline for API 17 and below
app:mrl_rippleInAdapter="true"            // if true, MaterialRippleLayout will optimize for use in AdapterViews
app:mrl_rippleDuration="350"              // duration of ripple animation
app:mrl_rippleFadeDuration="75"           // duration of fade out effect on ripple
app:mrl_rippleDelayClick="true"           // if true, delays calls to OnClickListeners until ripple effect ends
app:mrl_rippleBackground="#FFFFFF"        // background under ripple drawable; used with rippleOverlay="false"
app:mrl_ripplePersistent="true"           // if true, ripple background color persists after animation, until setRadius(0) is called
```

Set an `OnClickListener` to `MaterialRippleLayout`:

```java
findViewById(R.id.ripple).setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
        // handle me 
    }
});
```

Or if using in an `AdapterView`, simply use `OnItemClickListener`

Support for Android api versions <  14 
-----

For those unlucky developers that need to support older versions than 14, there's a way to do it.

You can use this library in addition with Jake Wharton's animation backport (http://nineoldandroids.com/) changing the imports from ` import android.animation.*;` to: ` import com.nineoldandroids.animation.*;` ,
`import android.util.Property`; to   `import com.nineoldandroids.util.Property;` and in MaterialRippleLayout.java file, calling function `shouldDelayChildPressedState()`  only if you're using api greater than 14.


Developed By
--------------------
Balys Valentukevicius

License
-----------

```
Copyright 2015 Balys Valentukevicius

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: https://raw.github.com/balysv/material-ripple/master/art/demo.gif
