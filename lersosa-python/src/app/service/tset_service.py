#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from ultralytics import YOLO

from app.common.config import ModelConfig
from app.controller.request import TestRequest
from app.model.entity import Test


# 测试业务逻辑
class TestService:

    # 获取所有数据
    @staticmethod
    async def read_items(param: TestRequest = None):
        # 测试传参
        if param is not None:
            print(param.image_base64)

        model = YOLO(model=ModelConfig.MODEL_PATH)
        data = model.predict(
            # source=param.base64_to_img(),
            source=ModelConfig.MODEL_SOURCES,
            save=False,
            show=False,
        )

        return Test(img_array=data[0].plot()).numpy_to_base64()
