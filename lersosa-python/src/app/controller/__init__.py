#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


import inspect
import os
from importlib import import_module

from app.base import BaseController

current_dir = os.path.dirname(os.path.abspath(__file__))
package_name = __package__

controllers = BaseController()

for file in os.listdir(current_dir):
    if file.endswith('.py') and not file.startswith("__"):
        module_name = file[:-3]
        full_module_name = f"{package_name}.{module_name}" if package_name else module_name
        imported_module = import_module(full_module_name)
        for name, obj in inspect.getmembers(imported_module):
            if inspect.isclass(obj) and issubclass(obj, BaseController):
                controllers.include_router(obj())

__all__ = ["controllers"]
