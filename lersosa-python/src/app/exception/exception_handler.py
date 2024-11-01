#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from fastapi import FastAPI, Request, HTTPException

from app.common import RepoResult
from app.model.enum import CodeStatus


class ExceptionHandlers:
    def __init__(self, app: FastAPI):
        self.app = app

    def add_exception_handlers(self):
        @self.app.exception_handler(HTTPException)
        async def http_exception_handler(_request: Request, exc: HTTPException):
            return RepoResult.error(code=exc.status_code, msg=exc.detail)

        @self.app.exception_handler(Exception)
        async def global_exception_handler(_request: Request, exc: Exception):
            return RepoResult.error(code=CodeStatus.INTERNAL_SERVER_ERROR.value, msg=str(exc))
