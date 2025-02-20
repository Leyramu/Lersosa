#
# Copyright (c) 2024 Leyramu. All rights reserved.
# This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
# For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
# The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
# By using this project, users acknowledge and agree to abide by these terms and conditions.
#

@echo off

cd /d %~dp0/../..
set rootDir=%cd%

docker-compose -f "%rootDir%/docker/docker-compose-prod.yml" --project-name leyramu_lersosa_prod up

echo Done
pause
endlocal
