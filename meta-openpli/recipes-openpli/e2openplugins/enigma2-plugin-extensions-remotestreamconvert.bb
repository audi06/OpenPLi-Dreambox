MODULE = "RemoteChannelStreamConverter"
DESCRIPTION = "Fetch channels from remote bouquets and make them available locally"

RDEPENDS:${PN} = "python3-shell"

PR="r0"

require conf/license/license-gplv2.inc
require openplugins-replace-pli.inc
require openplugins-distutils.inc

BRANCH = "master"
