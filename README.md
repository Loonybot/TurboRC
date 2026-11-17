# TurboRC

TurboRC is a library for FTC Robot Controllers. It runs one or more
low-priority background worker threads to encourage the CPU scheduler to raise the
clock speed. This is a hint, not a guaranteed performance increase. It can
increase power use and heat; use Sidekick RC to measure the impact. The turbo
boost won't engage until START is pressed, and will disengage when the op mode
is stopped or after 120 seconds.

## Installation

Add JitPack to the `repositories` block in your FTC project's root 
`build.dependencies.gradle`:

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

The default is one worker thread; use `new TurboRC(this, 2)` or
`new TurboRC(this, 3)` to use more. More workers may increase heat and compete
with robot code. Call `turbo.stop()` to stop them early, even before START;
its return value can be ignored. TurboRC only supports `LinearOpMode`.

## Verification

Use Sidekick RC to measure the CPU frequency during your op mode runs.
