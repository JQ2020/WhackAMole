# WhackAMole 打地鼠小游戏

基于Jetpack Compose实现的Android打地鼠小游戏，采用现代化MVVM架构设计。

<p align="center">
  <img src="screenshots/game_preview.png" alt="游戏预览" width="300"/>
</p>

## 项目简介

这是一个为Android设备开发的经典打地鼠游戏。游戏中，玩家需要在有限的时间内点击屏幕上随机出现的地鼠，每击中一只地鼠获得1分，击空则扣除2分，通过获取高分来挑战自我。

这个项目不仅是一个有趣的小游戏，也是一个展示现代Android开发技术和架构的示例项目。

## 功能特点

- 🎮 经典打地鼠游戏玩法，简单易上手
- 🎯 点击地鼠得分，击空扣分的挑战机制
- ⏱️ 限时挑战，提高游戏紧张感
- 📊 历史得分记录，追踪个人最佳成绩
- 🎵 音效反馈，增强游戏体验
- 📱 响应式UI设计，适配不同屏幕尺寸
- 📈 游戏结束展示分数统计

## 技术架构

项目采用**MVVM（Model-View-ViewModel）**架构，清晰分离业务逻辑和UI表现层：

- **View层**：使用Jetpack Compose构建的声明式UI
- **ViewModel层**：处理游戏逻辑，管理状态更新
- **Model层**：包含游戏状态数据结构和数据存储仓库

### 架构图
<p align="center">
  <img src="app/src/main/java/uml/architecture_preview.png" alt="架构图" width="600"/>
</p>

## 技术栈

- **UI框架**：Jetpack Compose
- **架构模式**：MVVM
- **状态管理**：StateFlow, MutableState
- **协程**：Kotlin Coroutines
- **动画**：Compose Animation
- **数据持久化**：SharedPreferences
- **依赖注入**：手动依赖注入

## 项目结构

```
com.example.myapplication/
├── MainActivity.kt            # 应用入口
├── ui/                        # UI相关组件
│   ├── App.kt                 # 主应用Composable
│   ├── screens/               # 屏幕UI组件
│   │   ├── GameScreen.kt      # 游戏主屏幕
│   │   └── HistoryScreen.kt   # 历史记录屏幕
│   ├── components/            # 可复用UI组件
│   │   └── MoleHole.kt        # 地鼠洞组件
│   └── theme/                 # 主题相关
├── viewmodel/                 # 视图模型
│   └── GameViewModel.kt       # 游戏视图模型
├── model/                     # 数据模型
│   └── GameState.kt           # 游戏状态数据类
├── repository/                # 数据仓库
│   └── ScoreRepository.kt     # 分数存储仓库
└── util/                      # 工具类
    └── SoundManager.kt        # 音效管理
```

## UML图表

项目包含多种UML图表，展示系统设计：

- **类图**：展示类之间的关系
- **序列图**：描述游戏流程
- **架构图**：展示分层设计
- **状态图**：呈现游戏状态转换
- **活动图**：展示用户操作流程
- **组件图**：说明模块交互
- **部署图**：描述运行环境
- **包图**：展示代码组织结构

UML图表位于 `app/src/main/java/uml/` 目录下。

## 如何运行

1. 克隆本仓库
2. 在Android Studio中打开项目
3. 使用虚拟设备或实体设备运行应用
4. 开始游戏！

## 游戏玩法

1. 点击"开始游戏"按钮开始
2. 当地鼠出现时，快速点击它们获得分数
3. 注意不要点击空洞，否则会扣分
4. 游戏限时20秒，时间结束后显示最终得分
5. 点击"历史记录"按钮查看历史最高分
6. 点击"重新开始"再次挑战

## 未来计划

- [ ] 增加难度级别选择
- [ ] 添加更多地鼠类型
- [ ] 实现在线排行榜
- [ ] 添加游戏道具
- [ ] 支持多主题切换

## 贡献指南

欢迎贡献代码、提出问题或建议！请遵循以下步骤：

1. Fork本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个Pull Request

## 许可证

本项目采用MIT许可证 - 详情见[LICENSE](LICENSE)文件
