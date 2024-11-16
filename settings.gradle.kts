rootProject.name = "com.ma7_8"
include("order-service")
include("common")
include("user-service:main")
findProject(":user-service:main")?.name = "main"
