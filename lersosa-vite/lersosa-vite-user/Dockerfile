FROM node:22-alpine AS base
ARG VERSION

FROM base AS deps

RUN apk add --no-cache libc6-compat

WORKDIR /lersosa-user

COPY . .
RUN set -eux; \
    npm install -g pnpm && pnpm i --frozen-lockfile;

FROM base AS builder

WORKDIR /lersosa-user

COPY --from=deps /lersosa-user/ .

RUN npm install -g pnpm && pnpm run build

FROM base AS runner
WORKDIR /lersosa-user

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

RUN mkdir .next
RUN chown nextjs:nodejs .next

ENV CONFIG_DIR=/lersosa-user/config NODE_ENV=production IS_DOCKER=1 VERSION=${VERSION}

COPY --from=builder /lersosa-user/public ./public
COPY --from=builder --chown=nextjs:nodejs /lersosa-user/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /lersosa-user/.next/static ./.next/static

USER nextjs

CMD ["node", "server.js"]
