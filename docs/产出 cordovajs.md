# 产出 cordova.js

本文重点说明在 `cordova-android` 平台中，核心的 `cordova.js` 是如何被编译、打包并最终注入到 Android 项目中的。

## 1. 核心编译命令

在 `cordova-android` 项目的 `package.json` 中，定义了以下 `scripts`：

```json
  "scripts": {
    "prepare": "cordova-js build > templates/project/assets/www/cordova.js",
    // ...
  }
```

当我们如果在 `cordova-android` 的源码目录下执行 `npm install` 工程依赖时，会自动触发 npm 的 `prepare` 钩子，或者你也可以直接手动运行：

```bash
npm run prepare
```

这条命令就是 cordova.js 生成的源头。

## 2. 编译原理与来源

`cordova.js` 并不是一个纯手写的单文件，而是一个**经过打包构建后的产物**。命令 `cordova-js build` 会执行以下合并逻辑：

### 2.1 依赖库：`cordova-js`
在 `devDependencies` 中，项目依赖了 `"cordova-js": "^6.1.0"` 开发包。`cordova-js` 仓库里存放了 Cordova 所有平台**公共的 JavaScript 核心 API**（例如 `cordova.js` 核、`require/define` 模块化系统、`modulemapper.js`、`builder.js`、`cordova/channel.js` 事件管道等）。

### 2.2 平台特有代码：`cordova-js-src`
每个平台（如 Android、iOS）都有其特定的 JS 桥接逻辑。在 `cordova-android` 根目录下有一个名为 `cordova-js-src` 的文件夹，里面存放了：
- `exec.js`：Android 专属的 `cordova.exec` JSBridge 桥接实现（区分了 PROMPT 模式和 EVAL_BRIDGE 模式等机制）。
- `platform.js`：定义了 Android 平台的初始化引导逻辑。
- `android/` 和 `plugin/android/`：存放平台相关的附加系统组件回调。

### 2.3 组合打包（Build 过程）
`cordova-js build` 脚本会自动检测当前执行上下文的平台（此时为 Android）。
它会将：
1. `cordova-js` 中的**核心公共代码**。
2. `cordova-android/cordova-js-src/` 目录下的 **Android 平台特有桥接代码**。

通过自定义的打包工具（Browserify 的变种或者自定义语法树组合合并），将所有的模块包装进内部实现的 `define`/`require` 模块系统中。组合后的标准输出流 (`stdout`) 被重定向（`>`）覆写到了目标路径下。

## 3. 产出物位置

打包后的 `cordova.js` 最终被输出到了特定的模板目录内：

```text
templates/project/assets/www/cordova.js
```

### 为什么是这个路径？

当你的用户在使用 Cordova CLI 创建项目，或者给项目**添加 android 平台**（例如 `cordova platform add android`）时，CLI 工具包底层依赖的就是这套代码。
CLI 会将 `templates/project/` 目录下的结构作为蓝本（Seed），完整地拷贝并生成到开发者的 `platforms/android/` 目录下去。
因此：
1. 预先生成的 `cordova.js` 一直呆在 `templates/.../www/` 目录下等待。
2. 开发者在构建自己的 App 时，这个 `cordova.js` 就会随着平台模板一起自动拷贝进他们项目的 `www` 目录里提供调用。

## 4. 如何进行自定义 Cordova.js 的开发与修改

如果你在修改 `cordova-android` 的底层逻辑，并且需要对 JSBridge 的前端部分（如 `cordova.exec` 的调度原理）进行定制：

1. **修改代码**：你需要修改的文件都在 `/Users/yuzhiqiang/workspace/personal/android/cordova-android/cordova-js-src/` 目录中。
   - 不要直接修改 `templates/project/assets/www/cordova.js`，因为这是编译产物，随时会被覆盖。
2. **重新编译**：完成修改后，在根目录执行 `npm run prepare` 更新打包产物。
3. **验证生效**：通过引入这套定制版的 `cordova-android` 框架（或者直接将其集成到你的 WebView 壳工程中通过 Asset 截获分发），App 侧请求加载 `cordova.js` 时即可包含你最新的修改。
