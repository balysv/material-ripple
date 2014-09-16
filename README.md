Material Ripple Layout
===============

Ripple effect wrapper for Android Views

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
app:rippleBackground="#FFFFFF" // background under ripple drawable; used with rippleOverlay="false"
```

Set an `OnClickListener` to `MaterialRippleLayout`:

```java
findViewById(R.id.ripple).setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
        // handle me 
    }
});
```

Access your child `View` if necessary:

```java
MyCustomView view = MaterialRippleLayout.getChildView();
```
