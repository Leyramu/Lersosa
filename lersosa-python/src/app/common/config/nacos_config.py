#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


import os


# Nacos 配置类
class NacosConfig:
    NACOS_SERVER_ADDR = os.getenv('NACOS_SERVER_ADDR', 'localhost:8848')
    NACOS_NAMESPACE = os.getenv('NACOS_NAMESPACE', '356d484c-399c-4a23-9419-e200e8edbff9')
    NACOS_GROUP = os.getenv('NACOS_GROUP', 'SERVICE_GROUP')
    NACOS_USERNAME = os.getenv('NACOS_USERNAME', 'nacos')
    NACOS_PASSWORD = os.getenv('NACOS_PASSWORD', 'Zcx@223852//')
    NACOS_DATA_ID = os.getenv('NACOS_DATA_ID', 'lersosa-service-python.yml')
    NACOS_SERVICE_NAME = os.getenv('NACOS_SERVICE_NAME', 'lersosa-service-python')
    HEARTBEAT_INTERVAL = int(os.getenv('HEARTBEAT_INTERVAL', '10'))
