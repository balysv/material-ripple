Material Ripple Layout
===============

Ripple effect wrapper for Android Views

![Demo Image][1]

Including in your project
-------------------------

Add Sonatype Maven repository and import dependencies

```groovy
repositories {
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots/"
    }
}

compile 'com.balysv:material-ripple:1.0.2-SNAPSHOT@aar'
compile 'com.nineoldandroids:library:2.4.0'
```

Usage
-----

Wrap your `View` with `MaterialRippleLayout` in your layout file:

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

Configure using xml attributes or appropriate setters in code:

```xml
app:rippleOverlay="true"       // if true, ripple is drawn in foreground; false - background
app:rippleColor="#ff0000"      // color of ripple
app:rippleAlpha="0.7"          // alpha of ripple
app:rippleDimension="35dp"     // radius of hover and starting ripple
app:rippleHover="false"        // if true, a hover effect is drawn when view is touched
app:rippleDuration="400"       // duration of ripple animation
app:rippleFadeDuration="50"    // duration of fade out effect on ripple
app:rippleBackground="#FFFFFF" // background under ripple drawable; used with rippleOverlay="false"
app:rippleDelayClick="true"    // if true, delays calls to OnClickListeners until ripple effect ends
app:ripplePersistent="true"    // if true, ripple background color persists after animation, until setRadius(0) is called
```Up

Set an `OnClickListener` to `MaterialRippleLayout`:

```java
findViewById(R.id.ripple).setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
        // handle me 
    }
});
```

Or if using in an `AdapterView`, simply use `OnItemClickListener`

Access your child `View` if necessary:

```java
MyCustomView view = MaterialRippleLayout.getChildView();
```

Developed By
--------------------
Balys Valentukevicius - <balys.v@gmail.com>

License
-----------

```
Copyright 2014 Balys Valentukevicius

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
