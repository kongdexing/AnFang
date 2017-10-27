package permissions.dispatcher.processor.impl

import com.squareup.javapoet.MethodSpec
import permissions.dispatcher.processor.RuntimePermissionsElement
import permissions.dispatcher.processor.util.typeMirrorOf
import javax.lang.model.type.TypeMirror

/**
 * ProcessorUnit implementation for Fragments defined in the support-v4 library.
 */
class SupportFragmentProcessorUnit: BaseProcessorUnit() {

    override fun getTargetType(): TypeMirror {
        return typeMirrorOf("android.support.v4.app.Fragment")
    }

    override fun checkPrerequisites(rpe: RuntimePermissionsElement) {
        // Nothing to check
    }

    override fun getActivityName(targetParam: String): String {
        return targetParam + ".getActivity()"
    }

    override fun addShouldShowRequestPermissionRationaleCondition(builder: MethodSpec.Builder, targetParam: String, permissionField: String, isPositiveCondition: Boolean) {
        builder.beginControlFlow("if (\$N\$T.shouldShowRequestPermissionRationale(\$N.getActivity(), \$N))", if (isPositiveCondition) "" else "!", PERMISSION_UTILS, targetParam, permissionField)
    }

    override fun addRequestPermissionsStatement(builder: MethodSpec.Builder, targetParam: String, permissionField: String, requestCodeField: String) {
        builder.addStatement("\$N.requestPermissions(\$N, \$N)", targetParam, permissionField, requestCodeField)
    }

}