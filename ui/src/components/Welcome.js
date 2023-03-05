import WelcomeInc from './WelcomeInc.js'

export const template = /*html*/`
<div class="normalContent">

<div class="ifSideNavbar">
  <WelcomeInc/>
</div>

</div>
`

export default { template, components: { WelcomeInc } }