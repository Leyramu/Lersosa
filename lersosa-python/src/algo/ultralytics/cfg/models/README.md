## Models

欢迎来到 [Ultralytics]（https://www.ultralytics.com/） Models 目录！在这里，您将找到各种预配置的模型配置文件 （'*.yaml's），可用于创建自定义 YOLO 模型。此目录中的模型由 Ultralytics 团队精心制作和微调，可为各种对象检测和图像分割任务提供最佳性能。

这些模型配置涵盖广泛的场景，从简单的对象检测到更复杂的任务，如实例分割和对象跟踪。它们还设计为在各种硬件平台（从 CPU 到 GPU）上高效运行。无论您是经验丰富的机器学习从业者还是刚刚开始使用 YOLO，此目录都为您的自定义模型开发需求提供了一个很好的起点。

要开始使用，只需浏览此目录中的模型并找到最适合您需求的模型。选择模型后，您可以使用提供的“*.yaml”文件轻松训练和部署您的自定义 YOLO 模型。在 Ultralytics [文档]（https://docs.ultralytics.com/models/） 上查看完整详细信息，如果您需要帮助或有任何疑问，请随时联系 Ultralytics 团队寻求支持。所以，不要等待，现在就开始创建您的自定义 YOLO 模型吧！
### Usage

Model `*.yaml` files may be used directly in the [Command Line Interface (CLI)](https://docs.ultralytics.com/usage/cli/) with a `yolo` command:

```bash
# Train a YOLO11n model using the coco8 dataset for 100 epochs
yolo task=detect mode=train model=yolo11n.yaml data=coco8.yaml epochs=100
```

They may also be used directly in a Python environment, and accept the same [arguments](https://docs.ultralytics.com/usage/cfg/) as in the CLI example above:

```python
from ultralytics import YOLO

# Initialize a YOLO11n model from a YAML configuration file
model = YOLO("model.yaml")

# If a pre-trained model is available, use it instead
# model = YOLO("model.pt")

# Display model information
model.info()

# Train the model using the COCO8 dataset for 100 epochs
model.train(data="coco8.yaml", epochs=100)
```

## Pre-trained Model Architectures

Ultralytics supports many model architectures. Visit [Ultralytics Models](https://docs.ultralytics.com/models/) to view detailed information and usage. Any of these models can be used by loading their configurations or pretrained checkpoints if available.

## Contribute New Models

Have you trained a new YOLO variant or achieved state-of-the-art performance with specific tuning? We'd love to showcase your work in our Models section! Contributions from the community in the form of new models, architectures, or optimizations are highly valued and can significantly enrich our repository.

By contributing to this section, you're helping us offer a wider array of model choices and configurations to the community. It's a fantastic way to share your knowledge and expertise while making the Ultralytics YOLO ecosystem even more versatile.

To get started, please consult our [Contributing Guide](https://docs.ultralytics.com/help/contributing/) for step-by-step instructions on how to submit a Pull Request (PR) 🛠️. Your contributions are eagerly awaited!

Let's join hands to extend the range and capabilities of the Ultralytics YOLO models 🙏!
