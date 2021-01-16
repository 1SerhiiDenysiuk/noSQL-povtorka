configs = {"fs.azure.account.auth.type": "OAuth",
           "fs.azure.account.oauth.provider.type": "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
           "fs.azure.account.oauth2.client.id": "93695c79-f39a-40e6-8642-accc9a3b93d4",
           "fs.azure.account.oauth2.client.secret": "YyUd-N32v6k5~__7QT30~RsY8Z.0d1ykPI",
           "fs.azure.account.oauth2.client.endpoint": "https://login.microsoftonline.com/127eebd6-263b-4cf3-8d2c-8614de14c8a1/oauth2/token",
           "fs.azure.createRemoteFileSystemDuringInitialization": "true"}

dbutils.fs.mount(
  source = "abfss://bdlabs@lab8storageacc.dfs.core.windows.net/",
  mount_point = "/mnt/labDenysiuk/",
  extra_configs = configs)

display(dbutils.fs.ls('/mnt/labDenysiuk/'))