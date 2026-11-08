# TurboRC

TurboRC is an Android library for FTC Robot Controllers. It runs one or more
low-priority background workers to encourage the CPU scheduler to raise the
clock speed. This is a hint, not a guaranteed performance increase; it can
increase power use and heat. Workers wait until Start, then stop when the
opMode stops or after 120 seconds.

## Installation

Once this repository is public and a release tag is available, add JitPack to
the `repositories` block in your FTC project's root `build.dependencies.gradle`:

```groovy
maven { url = 'https://jitpack.io' }
```

In the `dependencies` block of `TeamCode/build.gradle`, add:

```groovy
implementation 'com.github.Loonybot:TurboRC:<release-tag>'
```

Replace `<release-tag>` with a published TurboRC release tag, then sync Gradle.
You can also use a commit hash as the version once the repository is public.

## Usage

In a `LinearOpMode`, import and create TurboRC at the beginning of `runOpMode`:

```java
import com.loonybot.turborc.TurboRC;

// Inside runOpMode():
TurboRC turbo = new TurboRC(this);
```

The default is one worker; use `new TurboRC(this, 2)` or
`new TurboRC(this, 3)` to use more. More workers may increase heat and compete
with robot code. Call `turbo.stop()` to stop them early, even before Start;
its return value can be ignored. TurboRC only supports `LinearOpMode`.
